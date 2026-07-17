package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import sancho.model.mldonkey.enums.EnumNetwork;

class NetworkCollection$GetNetworkByEnum implements TObjectProcedure {
   EnumNetwork enumNetwork;
   Network foundNetworkInfo = null;

   public NetworkCollection$GetNetworkByEnum(EnumNetwork var1) {
      this.enumNetwork = var1;
   }

   public boolean execute(Object var1) {
      Network var2 = (Network)var1;
      if (var2.equals(this.enumNetwork)) {
         this.foundNetworkInfo = var2;
      }

      return true;
   }

   public Network getNetwork() {
      return this.foundNetworkInfo;
   }
}
