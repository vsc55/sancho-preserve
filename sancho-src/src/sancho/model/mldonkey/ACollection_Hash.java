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

   ACollection_Hash(ICore core) {
      this.core = core;
      this.infoMap = new THashMap();
   }

   public synchronized boolean containsKey(Object key) {
      return this.infoMap.contains(key);
   }

   public void dispose() {
      this.deleteObservers();
   }

   public synchronized Set entrySet() {
      return this.infoMap.entrySet();
   }

   public synchronized boolean forEachEntry(TObjectObjectProcedure procedure) {
      return this.infoMap.forEachEntry(procedure);
   }

   public synchronized Object get(Object key) {
      return this.infoMap.get(key);
   }

   public synchronized Set keySet() {
      return this.infoMap.keySet();
   }

   public synchronized Object put(Object key, Object value) {
      return this.infoMap.put(key, value);
   }

   public synchronized int size() {
      return this.infoMap.size();
   }
}
