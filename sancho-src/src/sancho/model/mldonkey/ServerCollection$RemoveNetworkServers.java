package sancho.model.mldonkey;

import gnu.trove.TIntObjectProcedure;
import sancho.model.mldonkey.enums.EnumNetwork;

class ServerCollection$RemoveNetworkServers implements TIntObjectProcedure {
   private EnumNetwork enumNetwork;

   public ServerCollection$RemoveNetworkServers(EnumNetwork var1) {
      this.enumNetwork = var1;
   }

   public boolean execute(int var1, Object var2) {
      Server var3 = (Server)var2;
      return this.enumNetwork != var3.getEnumNetwork();
   }
}
