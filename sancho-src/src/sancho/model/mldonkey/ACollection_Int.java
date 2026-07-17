package sancho.model.mldonkey;

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

   ACollection_Int(ICore var1) {
      this.core = var1;
      this.intObjectMap = new TIntObjectHashMap();
   }

   public final synchronized void clear() {
      this.intObjectMap.clear();
   }

   public final synchronized boolean containsKey(int var1) {
      return this.intObjectMap.contains(var1);
   }

   public void dispose() {
      this.deleteObservers();
   }

   public final synchronized boolean forEachValue(TObjectProcedure var1) {
      return this.intObjectMap.forEachValue(var1);
   }

   public final synchronized boolean forEachEntry(TIntObjectProcedure var1) {
      return this.intObjectMap.forEachEntry(var1);
   }

   public final synchronized Object get(int var1) {
      return this.intObjectMap.get(var1);
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

   public final synchronized Object put(int var1, Object var2) {
      return this.intObjectMap.put(var1, var2);
   }

   public synchronized Object remove(int var1) {
      return this.intObjectMap.remove(var1);
   }

   public final synchronized boolean retainEntries(TIntObjectProcedure var1) {
      return this.intObjectMap.retainEntries(var1);
   }

   public final synchronized int size() {
      return this.intObjectMap.size();
   }
}
