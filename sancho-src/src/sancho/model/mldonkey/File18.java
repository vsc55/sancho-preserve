package sancho.model.mldonkey;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File18 extends File {
   private Map availMap;

   File18(ICore core) {
      super(core);
   }

   protected void readAvailability(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (this.availMap == null) {
         this.availMap = new HashMap(count);
      } else {
         this.availMap.clear();
      }

      boolean multinetSeen = false;
      String previousAvail = this.getAvail();

      for (int i = 0; i < count; i++) {
         int networkId = buffer.getInt32();
         Network network = (Network)this.core.getNetworkCollection().get(networkId);
         if (network == null) {
            String avail = buffer.getString();
            Sancho.pDebug("readAvail: " + avail);
         } else if (network.getEnumNetwork() == EnumNetwork.MULTINET) {
            this.avail = buffer.getString();
            multinetSeen = true;
         } else {
            String avail = buffer.getString();
            if (!multinetSeen) {
               this.avail = avail;
            }

            this.availMap.put(network, avail);
         }
      }

      if (previousAvail != null && !previousAvail.equals(this.getAvail())) {
         this.addChangedBits(1024);
      }

      this.setRelativeAvail();
   }

   public boolean hasAvails() {
      return true;
   }

   public synchronized String getAvails(Network network) {
      return (String)this.availMap.get(network);
   }

   public synchronized Network[] getAllAvailNetworks() {
      if (this.availMap == null) {
         return new Network[0];
      } else {
         Network[] networks = new Network[this.availMap.size()];
         int i = 0;

         for (Iterator iterator = this.availMap.keySet().iterator(); iterator.hasNext(); i++) {
            networks[i] = (Network)iterator.next();
         }

         return networks;
      }
   }
}
