package sancho.model.mldonkey;

import gnu.trove.THashMap;
import gnu.trove.TObjectObjectProcedure;
import java.util.Set;
import sancho.core.ICore;
import sancho.utility.MyObservable;

public abstract class ACollection_Hash extends MyObservable implements ICollection {
   protected ICore core;
   protected THashMap infoMap;

   ACollection_Hash() {
      this(null);
   }

   ACollection_Hash(ICore var1) {
      this.core = var1;
      this.infoMap = new THashMap();
   }

   public synchronized boolean containsKey(Object var1) {
      return this.infoMap.contains(var1);
   }

   public void dispose() {
      this.deleteObservers();
   }

   public synchronized Set entrySet() {
      return this.infoMap.entrySet();
   }

   public synchronized boolean forEachEntry(TObjectObjectProcedure var1) {
      return this.infoMap.forEachEntry(var1);
   }

   public synchronized Object get(Object var1) {
      return this.infoMap.get(var1);
   }

   public synchronized Set keySet() {
      return this.infoMap.keySet();
   }

   public synchronized Object put(Object var1, Object var2) {
      return this.infoMap.put(var1, var2);
   }

   public synchronized int size() {
      return this.infoMap.size();
   }
}
