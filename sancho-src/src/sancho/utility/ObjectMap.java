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

   public ObjectMap(boolean weak) {
      if (weak) {
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

   public boolean add(Object key) {
      if (key == null) {
         return false;
      } else {
         boolean added = false;
         synchronized (this) {
            if (!this.mainMap.containsKey(key)) {
               this.mainMap.put(key, null);
               if (this.countObservers() > 0) {
                  this.getAddedMap().put(key, null);
               }

               added = true;
            }
         }

         if (added) {
            this.setChanged();
            this.notifyObservers(null, 0);
         }

         return added;
      }
   }

   public synchronized boolean containsKey(Object key) {
      return this.mainMap.containsKey(key);
   }

   public void remove(Object[] keys) {
      boolean removed = false;
      synchronized (this) {
         for (int i = 0; i < keys.length; i++) {
            if (this.mainMap.containsKey(keys[i])) {
               removed = true;
               this.mainMap.remove(keys[i]);
               if (this.countObservers() > 0) {
                  this.getRemovedMap().put(keys[i], null);
               }
            }
         }
      }

      if (removed) {
         this.setChanged();
         this.notifyObservers(null, 2);
      }
   }

   public void remove(Object key) {
      boolean removed = false;
      synchronized (this) {
         if (this.mainMap.containsKey(key)) {
            this.mainMap.remove(key);
            if (this.countObservers() > 0) {
               this.getRemovedMap().put(key, null);
            }

            removed = true;
         }
      }

      if (removed) {
         this.setChanged();
         this.notifyObservers(null, 2);
      }
   }

   public void addOrUpdate(Object key) {
      if (key != null) {
         boolean updated = false;
         if (!this.add(key)) {
            if (this.countObservers() > 0) {
               synchronized (this) {
                  this.getUpdatedMap().put(key, null);
               }
            }

            updated = true;
         }

         if (updated) {
            this.setChanged();
            this.notifyObservers(null, 1);
         }
      }
   }

   public synchronized boolean added() {
      return this.addedMap != null && this.addedMap.size() > 0;
   }

   public synchronized Object[] getAndClearAdded() {
      Object[] keys = SwissArmy.toArray(this.getAddedMap().keySet());
      this.clearAdded();
      return keys;
   }

   public synchronized boolean removed() {
      return this.removedMap != null && this.removedMap.size() > 0;
   }

   public synchronized Object[] getAndClearRemoved() {
      Object[] keys = SwissArmy.toArray(this.getRemovedMap().keySet());
      this.clearRemoved();
      return keys;
   }

   public synchronized boolean updated() {
      return this.updatedMap != null && this.updatedMap.size() > 0;
   }

   public synchronized Object removeFromMain(Object key) {
      return this.mainMap.remove(key);
   }

   public synchronized Object[] getAndClearUpdated() {
      Object[] keys = SwissArmy.toArray(this.getUpdatedMap().keySet());
      this.clearUpdated();
      return keys;
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

   public synchronized boolean contains(Object key) {
      return this.mainMap.containsKey(key);
   }

   public synchronized Object[] getKeyArray() {
      return SwissArmy.toArray(this.mainMap.keySet());
   }

   public synchronized int size() {
      return this.mainMap.size();
   }

   public synchronized Result getResult(int id) {
      Map map = this.mainMap;
      int remaining = map.size();

      try {
         Iterator iterator = map.keySet().iterator();

         while (--remaining >= 0) {
            try {
               Result result = (Result)iterator.next();
               if (result != null && result.getId() == id) {
                  return result;
               }
            } catch (NoSuchElementException noSuchElement) {
            }
         }
      } catch (NoSuchElementException noSuchElement) {
      }

      return null;
   }

   public void notifyObject(Object object) {
      this.setChanged();
      this.notifyObservers(object);
   }
}
