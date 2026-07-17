package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Room3 extends Room {
   private int numUsers;

   Room3(ICore var1) {
      super(var1);
   }

   public void read(int var1, MessageBuffer var2) {
      super.read(var1, var2);
      synchronized (this) {
         this.numUsers = var2.getInt32();
      }
   }

   public synchronized int getNumUsers() {
      return this.numUsers;
   }
}
