package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class ClientStats18 extends ClientStats {
   ClientStats18(ICore var1) {
      super(var1);
   }

   public void readNetworks(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      if (this.connectedNetworks == null || this.connectedNetworks.length != var2) {
         this.connectedNetworks = new Network[var2];
      }

      for (int var4 = 0; var4 < var2; var4++) {
         Network var3 = (Network)this.core.getNetworkCollection().get(var1.getInt32());
         if (var3 != null) {
            this.core.getNetworkCollection().setConnectedServers(var3, var1.getInt32());
         }

         this.connectedNetworks[var4] = var3;
      }
   }
}
