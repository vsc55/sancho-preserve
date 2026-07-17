package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class NetworkCollection extends ACollection_Int implements ICollection {
   public static int CHANGED_STATS = 1;

   NetworkCollection(ICore var1) {
      super(var1);
   }

   public Network getByEnum(EnumNetwork var1) {
      NetworkCollection$GetNetworkByEnum var2 = new NetworkCollection$GetNetworkByEnum(var1);
      this.forEachValue(var2);
      return var2.getNetwork();
   }

   public int getEnabledAndSearchable() {
      NetworkCollection$CountEnabledAndSearchable var1 = new NetworkCollection$CountEnabledAndSearchable();
      this.forEachValue(var1);
      return var1.getCount();
   }

   public EnumNetwork getNetworkEnum(int var1) {
      Network var2;
      return (var2 = (Network)this.get(var1)) != null ? var2.getEnumNetwork() : EnumNetwork.UNKNOWN;
   }

   public void getAllStats() {
      NetworkCollection$GetAllStats var1 = new NetworkCollection$GetAllStats();
      this.forEachValue(var1);
   }

   public String getAllNetworkStats(String var1) {
      NetworkCollection$GetNetworkStats var2 = new NetworkCollection$GetNetworkStats(var1);
      this.forEachValue(var2);
      return var2.getResultString();
   }

   public Network[] getNetworks() {
      Object[] var1 = this.getValues();
      Network[] var2 = new Network[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = (Network)var1[var3];
      }

      return var2;
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getInt32();
      boolean var3 = true;
      Network var4 = (Network)this.get(var2);
      if (var4 != null) {
         boolean var5 = var4.isEnabled();
         int var6 = var4.numConnectedServers();
         var4.read(var2, var1);
         if (var5 == var4.isEnabled() && var6 == var4.numConnectedServers()) {
            var3 = false;
         }
      } else {
         var4 = this.core.getCollectionFactory().getNetwork();
         var4.read(var2, var1);
         this.put(var2, var4);
         var4.getStats();
      }

      if (var3) {
         this.setChanged();
         this.notifyObservers(var4);
      }
   }

   public void readStats(MessageBuffer var1) {
      int var2 = var1.getInt32();
      Network var3 = (Network)this.get(var2);
      if (var3 != null) {
         var3.readStats(var1);
         int var4 = 0;
         var4 |= CHANGED_STATS;
         this.setChanged();
         this.notifyObservers(var3, var4);
      } else {
         Sancho.pDebug("readStats failed: " + var2);
      }
   }

   protected void setConnectedServers(Network var1, int var2) {
      if (var1 != null) {
         if (var1.numConnectedServers() != var2) {
            var1.setConnectedServers(var2);
            this.setChanged();
            this.notifyObservers(var1);
         }
      }
   }
}
