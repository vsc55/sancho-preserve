package sancho.model.mldonkey;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;
import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.SearchWaiting;
import sancho.model.mldonkey.utility.UtilityFactory;
import sancho.utility.ObjectMap;
import sancho.view.preferences.PreferenceLoader;
import gnu.trove.TIntObjectProcedure;

public class ResultCollection extends ACollection_Int {
   private TIntIntHashMap minAvailMap;
   public boolean filterProfanity;
   public boolean filterPornography;
   public boolean verboseNumbers = true;
   private TIntObjectHashMap resultIntMap = new TIntObjectHashMap();
   private TIntArrayList closedList;
   private TIntObjectHashMap searchResultChart;
   public int cnt;

   public synchronized int getNumResults() {
      return this.resultIntMap.size();
   }

   ResultCollection(ICore core) {
      super(core);
      this.minAvailMap = new TIntIntHashMap();
      this.closedList = new TIntArrayList();
      this.searchResultChart = new TIntObjectHashMap();
   }

   public synchronized void closeSearch(int searchId) {
      if (searchId < 0) {
         this.remove(searchId);
      } else {
         this.closedList.add(searchId);
         this.searchResultChart.remove(searchId);
         this.remove(searchId);
         Object[] payload = new Object[]{Integer.valueOf(searchId), Byte.valueOf((byte)1)};
         this.core.send((short)53, payload);
      }
   }

   public void download(Result result, boolean force) {
      int resultId = result.getId();
      if (resultId < 0) {
         this.core.send((short)8, result.getED2K());
      } else {
         Object[] payload = new Object[]{result.getNames(), Integer.valueOf(resultId), Byte.valueOf((byte)(force ? 1 : 0))};
         this.core.send((short)50, payload);
      }
   }

   public synchronized boolean alreadyClosed(int searchId) {
      return this.closedList.contains(searchId);
   }

   public synchronized Result getResult(int resultId) {
      return (Result)this.resultIntMap.get(resultId);
   }

   protected boolean hasMinAvail(int searchId, int avail) {
      synchronized (this.minAvailMap) {
         return avail >= this.minAvailMap.get(searchId);
      }
   }

   public void read(MessageBuffer buffer) {
      int searchId = buffer.getInt32();
      int resultId = buffer.getInt32();
      Result result = null;
      synchronized (this) {
         if (this.resultIntMap.contains(resultId)) {
            result = (Result)this.resultIntMap.get(resultId);
            this.resultIntMap.remove(resultId);
            TIntArrayList resultIds = (TIntArrayList)this.searchResultChart.get(searchId);
            if (resultIds == null) {
               resultIds = new TIntArrayList();
               this.searchResultChart.put(searchId, resultIds);
            }

            if (!resultIds.contains(resultId)) {
               resultIds.add(resultId);
            }
         } else {
            TIntArrayList resultIds = (TIntArrayList)this.searchResultChart.get(searchId);
            if (resultIds == null || !resultIds.contains(resultId)) {
               return;
            }

            ObjectMap resultMap = (ObjectMap)this.get(searchId);
            if (resultMap != null) {
               result = resultMap.getResult(resultId);
            }
         }
      }

      if (result != null) {
         if (this.hasMinAvail(searchId, result.getAvail())) {
            boolean changed = false;
            if (this.containsKey(searchId)) {
               ((ObjectMap)this.get(searchId)).addOrUpdate(result);
               changed = true;
            } else if (!this.alreadyClosed(searchId)) {
               ObjectMap resultMap = new ObjectMap(false);
               resultMap.add(result);
               this.put(searchId, resultMap);
               changed = true;
            }

            if (changed) {
               this.setChanged();
               this.notifyObservers(this);
            }
         }
      }
   }

   public void resultInfo(MessageBuffer buffer) {
      int resultId = buffer.getInt32();
      Result result = (Result)this.resultMapGet(resultId);
      if (result != null) {
         result.read(resultId, buffer);
      } else {
         result = this.findResult(resultId);
         if (result != null) {
            result.read(resultId, buffer);
         } else {
            result = this.core.getCollectionFactory().getResult();
            result.read(resultId, buffer);
            this.resultMapPut(resultId, result);
         }
      }
   }

   public synchronized Result findResult(int resultId) {
      int[] searchIds = this.searchResultChart.keys();

      for (int i = 0; i < searchIds.length; i++) {
         int searchId = searchIds[i];
         TIntArrayList resultIds = (TIntArrayList)this.searchResultChart.get(searchId);
         if (resultIds != null && resultIds.contains(resultId)) {
            ObjectMap resultMap = (ObjectMap)this.get(searchId);
            if (resultMap != null) {
               Result result = resultMap.getResult(resultId);
               if (result != null) {
                  return result;
               }
            }
         }
      }

      return null;
   }

   public synchronized Object resultMapGet(int resultId) {
      return this.resultIntMap.get(resultId);
   }

   public synchronized boolean resultMapContains(int resultId) {
      return this.resultIntMap.contains(resultId);
   }

   public synchronized void resultMapPut(int resultId, Object result) {
      this.resultIntMap.put(resultId, result);
   }

   public void updatePreferences() {
      this.filterPornography = PreferenceLoader.loadBoolean("searchFilterPornography");
      this.filterProfanity = PreferenceLoader.loadBoolean("searchFilterProfanity");
      this.verboseNumbers = PreferenceLoader.loadBoolean("verboseNumbers");
   }

   public void searchWaiting(MessageBuffer buffer) {
      SearchWaiting searchWaiting = UtilityFactory.getSearchWaiting(this.core);
      searchWaiting.read(buffer);
      if (this.containsKey(searchWaiting.getId())) {
         ((ObjectMap)this.get(searchWaiting.getId())).notifyObject(searchWaiting);
      } else {
         this.setChanged();
         this.notifyObservers(searchWaiting);
      }
   }

   public void setMinAvail(int searchId, int minAvail) {
      synchronized (this.minAvailMap) {
         this.minAvailMap.put(searchId, minAvail);
      }
   }

   public String getStats() {
      GetCollectionStats collector = new GetCollectionStats();
      this.forEachEntry(collector);
      return collector.getStats();
   }

   // Trove forEachEntry: build a compact [searchId|resultCount] stats string.
   private static class GetCollectionStats implements TIntObjectProcedure {
      private StringBuffer sb = new StringBuffer();

      public GetCollectionStats() {
      }

      public boolean execute(int searchId, Object value) {
         ObjectMap resultMap = (ObjectMap)value;
         this.sb.append("[");
         this.sb.append(searchId);
         this.sb.append("|");
         this.sb.append(resultMap.size());
         this.sb.append("]");
         return true;
      }

      public String getStats() {
         return this.sb.toString().intern();
      }
   }
}
