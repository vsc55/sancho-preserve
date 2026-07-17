package sancho.model.mldonkey;

import java.util.Map;
import java.util.WeakHashMap;
import sancho.core.ICore;
import sancho.utility.SwissArmy;

public abstract class ACollection_Int2 extends ACollection_Int {
   private Map addedMap = new WeakHashMap();
   private Map removedMap = new WeakHashMap();
   private Map updatedMap = new WeakHashMap();

   ACollection_Int2(ICore var1) {
      super(var1);
   }

   public synchronized boolean added() {
      return this.addedMap.size() > 0;
   }

   public synchronized void addToAdded(Object var1) {
      if (var1 != null && this.countObservers() > 0) {
         this.addedMap.put(var1, null);
      }
   }

   public synchronized void addToRemoved(Object var1) {
      if (var1 != null && this.countObservers() > 0) {
         this.removedMap.put(var1, null);
      }
   }

   public synchronized void addToUpdated(Object var1) {
      if (var1 != null && this.countObservers() > 0) {
         this.updatedMap.put(var1, null);
      }
   }

   public synchronized void clearAdded() {
      SwissArmy.clear(this.addedMap);
   }

   public synchronized void clearAllLists() {
      SwissArmy.clear(this.addedMap);
      SwissArmy.clear(this.removedMap);
      SwissArmy.clear(this.updatedMap);
   }

   public synchronized void clearRemoved() {
      SwissArmy.clear(this.removedMap);
   }

   public synchronized void clearUpdated() {
      SwissArmy.clear(this.updatedMap);
   }

   public synchronized Object[] getAndClearAdded() {
      Object[] var1 = SwissArmy.toArray(this.addedMap.keySet());
      this.clearAdded();
      return var1;
   }

   public synchronized Object[] getAndClearRemoved() {
      Object[] var1 = SwissArmy.toArray(this.removedMap.keySet());
      this.clearRemoved();
      return var1;
   }

   public synchronized Object[] getAndClearUpdated() {
      Object[] var1 = SwissArmy.toArray(this.updatedMap.keySet());
      this.clearUpdated();
      return var1;
   }

   public synchronized boolean removed() {
      return this.removedMap.size() > 0;
   }

   public synchronized boolean updated() {
      return this.updatedMap.size() > 0;
   }
}
