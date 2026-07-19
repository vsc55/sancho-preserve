package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class ClientStats18 extends ClientStats {
   ClientStats18(ICore core) {
      super(core);
   }

   public void readNetworks(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (this.connectedNetworks == null || this.connectedNetworks.length != count) {
         this.connectedNetworks = new Network[count];
      }

      for (int i = 0; i < count; i++) {
         // Each proto>=18 entry is (network_id, connected_servers) — BOTH int32s are
         // always on the wire. Read both unconditionally; reading the count only when
         // the network was known desynced the rest of the message (the unread count got
         // consumed as the next network id) whenever a network id wasn't registered yet.
         int networkId = buffer.getInt32();
         int connectedServers = buffer.getInt32();
         Network network = (Network)this.core.getNetworkCollection().get(networkId);
         if (network != null) {
            this.core.getNetworkCollection().setConnectedServers(network, connectedServers);
         }

         this.connectedNetworks[i] = network;
      }
   }
}
