package sancho.model.mldonkey.utility;

public class NetworkStatCollection {
   String name;
   int uptimeSeconds;
   NetworkStat[] networkStatArray;
   int tSeen = 0;
   int tBanned = 0;
   int tRequest = 0;
   long tDownload = 0L;
   long tUpload = 0L;

   public synchronized int getSeen() {
      return this.tSeen;
   }

   public synchronized int getBanned() {
      return this.tBanned;
   }

   public synchronized int getRequest() {
      return this.tRequest;
   }

   public synchronized long getDownload() {
      return this.tDownload;
   }

   public synchronized long getUpload() {
      return this.tUpload;
   }

   public synchronized NetworkStat[] getNetworkStatArray() {
      return this.networkStatArray;
   }

   public synchronized int getUptime() {
      return this.uptimeSeconds;
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.name = var1.getString();
         this.uptimeSeconds = var1.getInt32();
         int var3 = var1.getUInt16();
         this.networkStatArray = null;
         if (var3 > 0) {
            this.tSeen = 0;
            this.tBanned = 0;
            this.tRequest = 0;
            this.tDownload = 0L;
            this.tUpload = 0L;
            this.networkStatArray = new NetworkStat[var3 + 1];

            for (int var4 = 0; var4 < var3; var4++) {
               NetworkStat var5 = UtilityFactory.getNetworkStat(this);
               var5.read(var1);
               this.networkStatArray[var4] = var5;
               this.tSeen = this.tSeen + var5.getSeen();
               this.tBanned = this.tBanned + var5.getBanned();
               this.tRequest = this.tRequest + var5.getRequest();
               this.tDownload = this.tDownload + var5.getDownload();
               this.tUpload = this.tUpload + var5.getUpload();
            }

            NetworkStatTotal var8 = UtilityFactory.getNetworkStatTotal(this);
            var8.read(this.tSeen, this.tBanned, this.tRequest, this.tDownload, this.tUpload);
            this.networkStatArray[var3] = var8;
         }
      }
   }
}
