package sancho.model.mldonkey.utility;

public class Addr34 extends Addr {
   boolean blocked;

   public void read(MessageBuffer buffer) {
      this.read(buffer.getBool(), buffer);
      synchronized (this) {
         this.blocked = buffer.getBool();
      }
   }

   public synchronized boolean isBlocked() {
      return this.blocked;
   }
}
