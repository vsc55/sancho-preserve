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

   public void read(MessageBuffer buffer) {
      synchronized (this) {
         this.name = buffer.getString();
         this.uptimeSeconds = buffer.getInt32();
         int count = buffer.getUInt16();
         this.networkStatArray = null;
         if (count > 0) {
            this.tSeen = 0;
            this.tBanned = 0;
            this.tRequest = 0;
            this.tDownload = 0L;
            this.tUpload = 0L;
            this.networkStatArray = new NetworkStat[count + 1];

            for (int i = 0; i < count; i++) {
               NetworkStat stat = UtilityFactory.getNetworkStat(this);
               stat.read(buffer);
               this.networkStatArray[i] = stat;
               this.tSeen = this.tSeen + stat.getSeen();
               this.tBanned = this.tBanned + stat.getBanned();
               this.tRequest = this.tRequest + stat.getRequest();
               this.tDownload = this.tDownload + stat.getDownload();
               this.tUpload = this.tUpload + stat.getUpload();
            }

            NetworkStatTotal total = UtilityFactory.getNetworkStatTotal(this);
            total.read(this.tSeen, this.tBanned, this.tRequest, this.tDownload, this.tUpload);
            this.networkStatArray[count] = total;
         }
      }
   }
}
