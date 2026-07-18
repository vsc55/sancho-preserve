package sancho.model.mldonkey;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TIntObjectIterator;
import gnu.trove.TIntObjectProcedure;
import gnu.trove.TObjectProcedure;
import sancho.core.ICore;
import sancho.utility.MyObservable;

public abstract class ACollection_Int extends MyObservable implements ICollection {
   protected ICore core;
   private TIntObjectHashMap intObjectMap;

   ACollection_Int() {
      this(null);
   }

   ACollection_Int(ICore core) {
      this.core = core;
      this.intObjectMap = new TIntObjectHashMap();
   }

   public final synchronized void clear() {
      this.intObjectMap.clear();
   }

   public final synchronized boolean containsKey(int id) {
      return this.intObjectMap.contains(id);
   }

   public void dispose() {
      this.deleteObservers();
   }

   public final synchronized boolean forEachValue(TObjectProcedure procedure) {
      return this.intObjectMap.forEachValue(procedure);
   }

   public final synchronized boolean forEachEntry(TIntObjectProcedure procedure) {
      return this.intObjectMap.forEachEntry(procedure);
   }

   public final synchronized Object get(int id) {
      return this.intObjectMap.get(id);
   }

   public final ICore getCore() {
      return this.core;
   }

   public final synchronized Object[] getValues() {
      return this.intObjectMap.getValues();
   }

   public final TIntObjectIterator iterator() {
      return this.intObjectMap.iterator();
   }

   public final synchronized Object put(int id, Object value) {
      return this.intObjectMap.put(id, value);
   }

   public synchronized Object remove(int id) {
      return this.intObjectMap.remove(id);
   }

   public final synchronized boolean retainEntries(TIntObjectProcedure procedure) {
      return this.intObjectMap.retainEntries(procedure);
   }

   public final synchronized int size() {
      return this.intObjectMap.size();
   }

   // Trove retainEntries filter: keep only the entries whose int key is in the given list
   // (used to prune a collection down to the ids the core just reported).
   static class CleanIntMap implements TIntObjectProcedure {
      TIntArrayList retainIntList;

      public CleanIntMap(TIntArrayList retainIntList) {
         this.retainIntList = retainIntList;
      }

      public boolean execute(int id, Object value) {
         return this.retainIntList.contains(id);
      }
   }
}
