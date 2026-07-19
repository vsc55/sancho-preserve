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

   Server40(ICore core) {
      super(core);
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

   protected void read40(MessageBuffer buffer) {
      this.version = buffer.getString();
      this.maxUsers = buffer.getUInt64();
      this.lowidUsers = buffer.getUInt64();
      this.softLimit = buffer.getUInt64();
      this.hardLimit = buffer.getUInt64();
      this.ping = buffer.getInt32();
   }
}
