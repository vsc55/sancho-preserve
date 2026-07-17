package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import sancho.model.mldonkey.enums.EnumNetwork;

class ServerCollection$CountDonkeyTotals implements TObjectProcedure {
   private long numFiles;
   private long numUsers;
   private long numServers;

   public boolean execute(Object var1) {
      Server var2 = (Server)var1;
      if (var2.getEnumNetwork() == EnumNetwork.DONKEY) {
         this.numServers++;
         this.numUsers = this.numUsers + var2.getNumUsers();
         this.numFiles = this.numFiles + var2.getNumFiles();
      }

      return true;
   }

   public long getNumServers() {
      return this.numServers;
   }

   public long getNumFiles() {
      return this.numFiles;
   }

   public long getNumUsers() {
      return this.numUsers;
   }
}
