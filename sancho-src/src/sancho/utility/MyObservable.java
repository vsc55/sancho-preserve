package sancho.utility;

import java.util.Vector;

public class MyObservable {
   private boolean changed = false;
   private Vector obs;

   public synchronized void addObserver(MyObserver var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (this.obs == null) {
            this.obs = new Vector();
         }

         if (!this.obs.contains(var1)) {
            this.obs.addElement(var1);
         }
      }
   }

   public synchronized void deleteObserver(MyObserver var1) {
      if (this.obs != null) {
         this.obs.removeElement(var1);
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

   public void notifyObservers(Object var1) {
      this.notifyObservers(var1, 0);
   }

   public void notifyObservers(Object var1, int var2) {
      Object[] var3;
      synchronized (this) {
         if (!this.changed || this.obs == null) {
            return;
         }

         var3 = this.obs.toArray();
         this.clearChanged();
      }

      for (int var5 = var3.length - 1; var5 >= 0; var5--) {
         ((MyObserver)var3[var5]).update(this, var1, var2);
      }
   }
}
