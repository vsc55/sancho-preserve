package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import sancho.model.mldonkey.enums.EnumNetwork;

class ServerCollection$CountNetworkConnected implements TObjectProcedure {
   private int counter;
   private EnumNetwork networkEnum;

   public ServerCollection$CountNetworkConnected(EnumNetwork var1) {
      this.networkEnum = var1;
   }

   public boolean execute(Object var1) {
      Server var2 = (Server)var1;
      if (var2.isConnected() && var2.getEnumNetwork() == this.networkEnum) {
         this.counter++;
      }

      return true;
   }

   public int getCount() {
      return this.counter;
   }
}
