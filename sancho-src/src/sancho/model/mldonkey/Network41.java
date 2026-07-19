package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.model.mldonkey.utility.UtilityFactory;

public class Network41 extends Network18 {
   NetworkStatCollection[] networkStatCollectionArray;

   Network41(ICore core) {
      super(core);
   }

   public void getStats() {
      Object[] args = new Object[]{Integer.valueOf(this.getId())};
      this.core.send((short)68, args);
   }

   public synchronized NetworkStatCollection[] getNetworkStatCollection() {
      if (this.networkStatCollectionArray == null) {
         return null;
      } else {
         NetworkStatCollection[] statCollections = new NetworkStatCollection[this.networkStatCollectionArray.length];
         System.arraycopy(this.networkStatCollectionArray, 0, statCollections, 0, this.networkStatCollectionArray.length);
         return statCollections;
      }
   }

   public void readStats(MessageBuffer buffer) {
      synchronized (this) {
         int count = buffer.getUInt16();
         if (count > 0 && (this.networkStatCollectionArray == null || count != this.networkStatCollectionArray.length)) {
            this.networkStatCollectionArray = new NetworkStatCollection[count];
         }

         if (count > 0) {
            for (int i = 0; i < count; i++) {
               NetworkStatCollection statCollection = this.networkStatCollectionArray[i];
               if (statCollection == null) {
                  statCollection = UtilityFactory.getNetworkStatCollection();
               }

               statCollection.read(buffer);
               this.networkStatCollectionArray[i] = statCollection;
            }
         }
      }
   }
}
