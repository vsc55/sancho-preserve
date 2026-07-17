package sancho.model.mldonkey.utility;

public class Addr34 extends Addr {
   boolean blocked;

   public void read(MessageBuffer var1) {
      this.read(var1.getBool(), var1);
      synchronized (this) {
         this.blocked = var1.getBool();
      }
   }

   public synchronized boolean isBlocked() {
      return this.blocked;
   }
}
