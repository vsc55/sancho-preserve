package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Server40 extends Server32 {
   String version;
   long maxUsers;
   long softLimit;
   long hardLimit;
   long lowidUsers;
   int ping;

   Server40(ICore var1) {
      super(var1);
   }

   public synchronized String getVersion() {
      return this.version;
   }

   public synchronized long getMaxUsers() {
      return this.maxUsers;
   }

   public synchronized long getSoftLimit() {
      return this.softLimit;
   }

   public synchronized long getHardLimit() {
      return this.hardLimit;
   }

   public synchronized long getLowIDUsers() {
      return this.lowidUsers;
   }

   public synchronized int getPing() {
      return this.ping;
   }

   protected void read40(MessageBuffer var1) {
      this.version = var1.getString();
      this.maxUsers = var1.getUInt64();
      this.lowidUsers = var1.getUInt64();
      this.softLimit = var1.getUInt64();
      this.hardLimit = var1.getUInt64();
      this.ping = var1.getInt32();
   }
}
