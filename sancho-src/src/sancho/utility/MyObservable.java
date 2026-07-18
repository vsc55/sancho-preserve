package sancho.utility;

import java.util.Vector;

public class MyObservable {
   private boolean changed = false;
   private Vector obs;

   public synchronized void addObserver(MyObserver observer) {
      if (observer == null) {
         throw new NullPointerException();
      } else {
         if (this.obs == null) {
            this.obs = new Vector();
         }

         if (!this.obs.contains(observer)) {
            this.obs.addElement(observer);
         }
      }
   }

   public synchronized void deleteObserver(MyObserver observer) {
      if (this.obs != null) {
         this.obs.removeElement(observer);
      }
   }

   public synchronized void deleteObservers() {
      if (this.obs != null) {
         this.obs.removeAllElements();
      }
   }

   protected synchronized void setChanged() {
      this.changed = true;
   }

   protected synchronized void clearChanged() {
      this.changed = false;
   }

   public synchronized boolean hasChanged() {
      return this.changed;
   }

   public synchronized int countObservers() {
      return this.obs == null ? 0 : this.obs.size();
   }

   public void notifyObservers() {
      this.notifyObservers(null);
   }

   public void notifyObservers(Object arg) {
      this.notifyObservers(arg, 0);
   }

   public void notifyObservers(Object arg, int flags) {
      Object[] observers;
      synchronized (this) {
         if (!this.changed || this.obs == null) {
            return;
         }

         observers = this.obs.toArray();
         this.clearChanged();
      }

      for (int i = observers.length - 1; i >= 0; i--) {
         ((MyObserver)observers[i]).update(this, arg, flags);
      }
   }
}
