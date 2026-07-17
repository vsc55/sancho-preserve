package sancho.utility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import sancho.model.mldonkey.Result;

public class ObjectMap extends MyObservable {
   public static final int ADDED = 0;
   public static final int UPDATED = 1;
   public static final int REMOVED = 2;
   private Map mainMap;
   private Map addedMap;
   private Map removedMap;
   private Map updatedMap;

   public ObjectMap(boolean var1) {
      if (var1) {
         this.mainMap = new WeakHashMap();
      } else {
         this.mainMap = new HashMap();
      }
   }

   private synchronized Map getAddedMap() {
      if (this.addedMap == null) {
         this.addedMap = new WeakHashMap();
      }

      return this.addedMap;
   }

   private synchronized Map getRemovedMap() {
      if (this.removedMap == null) {
         this.removedMap = new WeakHashMap();
      }

      return this.removedMap;
   }

   private synchronized Map getUpdatedMap() {
      if (this.updatedMap == null) {
         this.updatedMap = new WeakHashMap();
      }

      return this.updatedMap;
   }

   public boolean add(Object var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;
         synchronized (this) {
            if (!this.mainMap.containsKey(var1)) {
               this.mainMap.put(var1, null);
               if (this.countObservers() > 0) {
                  this.getAddedMap().put(var1, null);
               }

               var2 = true;
            }
         }

         if (var2) {
            this.setChanged();
            this.notifyObservers(null, 0);
         }

         return var2;
      }
   }

   public synchronized boolean containsKey(Object var1) {
      return this.mainMap.containsKey(var1);
   }

   public void remove(Object[] var1) {
      boolean var2 = false;
      synchronized (this) {
         for (int var4 = 0; var4 < var1.length; var4++) {
            if (this.mainMap.containsKey(var1[var4])) {
               var2 = true;
               this.mainMap.remove(var1[var4]);
               if (this.countObservers() > 0) {
                  this.getRemovedMap().put(var1[var4], null);
               }
            }
         }
      }

      if (var2) {
         this.setChanged();
         this.notifyObservers(null, 2);
      }
   }

   public void remove(Object var1) {
      boolean var2 = false;
      synchronized (this) {
         if (this.mainMap.containsKey(var1)) {
            this.mainMap.remove(var1);
            if (this.countObservers() > 0) {
               this.getRemovedMap().put(var1, null);
            }

            var2 = true;
         }
      }

      if (var2) {
         this.setChanged();
         this.notifyObservers(null, 2);
      }
   }

   public void addOrUpdate(Object var1) {
      if (var1 != null) {
         boolean var2 = false;
         if (!this.add(var1)) {
            if (this.countObservers() > 0) {
               synchronized (this) {
                  this.getUpdatedMap().put(var1, null);
               }
            }

            var2 = true;
         }

         if (var2) {
            this.setChanged();
            this.notifyObservers(null, 1);
         }
      }
   }

   public synchronized boolean added() {
      return this.addedMap != null && this.addedMap.size() > 0;
   }

   public synchronized Object[] getAndClearAdded() {
      Object[] var1 = SwissArmy.toArray(this.getAddedMap().keySet());
      this.clearAdded();
      return var1;
   }

   public synchronized boolean removed() {
      return this.removedMap != null && this.removedMap.size() > 0;
   }

   public synchronized Object[] getAndClearRemoved() {
      Object[] var1 = SwissArmy.toArray(this.getRemovedMap().keySet());
      this.clearRemoved();
      return var1;
   }

   public synchronized boolean updated() {
      return this.updatedMap != null && this.updatedMap.size() > 0;
   }

   public synchronized Object removeFromMain(Object var1) {
      return this.mainMap.remove(var1);
   }

   public synchronized Object[] getAndClearUpdated() {
      Object[] var1 = SwissArmy.toArray(this.getUpdatedMap().keySet());
      this.clearUpdated();
      return var1;
   }

   public synchronized void clearAdded() {
      if (this.addedMap != null) {
         SwissArmy.clear(this.addedMap);
      }
   }

   public synchronized void clearRemoved() {
      if (this.removedMap != null) {
         SwissArmy.clear(this.removedMap);
      }
   }

   public synchronized void clearUpdated() {
      if (this.updatedMap != null) {
         SwissArmy.clear(this.updatedMap);
      }
   }

   public synchronized void clearAllLists() {
      this.clearAdded();
      this.clearRemoved();
      this.clearUpdated();
   }

   public synchronized boolean contains(Object var1) {
      return this.mainMap.containsKey(var1);
   }

   public synchronized Object[] getKeyArray() {
      return SwissArmy.toArray(this.mainMap.keySet());
   }

   public synchronized int size() {
      return this.mainMap.size();
   }

   public synchronized Result getResult(int var1) {
      Map var2 = this.mainMap;
      int var3 = var2.size();

      try {
         Iterator var4 = var2.keySet().iterator();

         while (--var3 >= 0) {
            try {
               Result var5 = (Result)var4.next();
               if (var5 != null && var5.getId() == var1) {
                  return var5;
               }
            } catch (NoSuchElementException var6) {
            }
         }
      } catch (NoSuchElementException var7) {
      }

      return null;
   }

   public void notifyObject(Object var1) {
      this.setChanged();
      this.notifyObservers(var1);
   }
}
