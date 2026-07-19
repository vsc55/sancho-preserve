package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Room3 extends Room {
   private int numUsers;

   Room3(ICore core) {
      super(core);
   }

   public void read(int id, MessageBuffer buffer) {
      super.read(id, buffer);
      synchronized (this) {
         this.numUsers = buffer.getInt32();
      }
   }

   public synchronized int getNumUsers() {
      return this.numUsers;
   }
}
