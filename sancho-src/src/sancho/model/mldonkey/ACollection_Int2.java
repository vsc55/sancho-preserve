package sancho.model.mldonkey;

import java.util.Map;
import java.util.WeakHashMap;
import sancho.core.ICore;
import sancho.utility.SwissArmy;

public abstract class ACollection_Int2 extends ACollection_Int {
   private Map addedMap = new WeakHashMap();
   private Map removedMap = new WeakHashMap();
   private Map updatedMap = new WeakHashMap();

   ACollection_Int2(ICore core) {
      super(core);
   }

   public synchronized boolean added() {
      return this.addedMap.size() > 0;
   }

   public synchronized void addToAdded(Object object) {
      if (object != null && this.countObservers() > 0) {
         this.addedMap.put(object, null);
      }
   }

   public synchronized void addToRemoved(Object object) {
      if (object != null && this.countObservers() > 0) {
         this.removedMap.put(object, null);
      }
   }

   public synchronized void addToUpdated(Object object) {
      if (object != null && this.countObservers() > 0) {
         this.updatedMap.put(object, null);
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
      Object[] values = SwissArmy.toArray(this.addedMap.keySet());
      this.clearAdded();
      return values;
   }

   public synchronized Object[] getAndClearRemoved() {
      Object[] values = SwissArmy.toArray(this.removedMap.keySet());
      this.clearRemoved();
      return values;
   }

   public synchronized Object[] getAndClearUpdated() {
      Object[] values = SwissArmy.toArray(this.updatedMap.keySet());
      this.clearUpdated();
      return values;
   }

   public synchronized boolean removed() {
      return this.removedMap.size() > 0;
   }

   public synchronized boolean updated() {
      return this.updatedMap.size() > 0;
   }
}
