package sancho.model.mldonkey.utility;

import sancho.utility.SwissArmy;

public class NetworkStat {
   NetworkStatCollection networkStatCollection;
   String stringLong;
   String stringShort;
   int seen;
   int banned;
   int filerequest;
   long download;
   long upload;

   NetworkStat(NetworkStatCollection collection) {
      this.networkStatCollection = collection;
   }

   public int getUptime() {
      return this.networkStatCollection.getUptime();
   }

   public synchronized String getNameLong() {
      return this.stringLong;
   }

   public synchronized String getNameShort() {
      return this.stringShort;
   }

   public synchronized int getSeen() {
      return this.seen;
   }

   public int getSeenPercent() {
      float total = (float)this.networkStatCollection.getSeen();
      return total <= 0.0F ? 0 : (int)((float)this.getSeen() / total * 100.0F);
   }

   public String getSeenPercentString() {
      return String.valueOf(this.getSeenPercent());
   }

   public synchronized int getBanned() {
      return this.banned;
   }

   public int getBannedPercent() {
      float total = (float)this.networkStatCollection.getBanned();
      return total <= 0.0F ? 0 : (int)((float)this.getBanned() / total * 100.0F);
   }

   public String getBannedPercentString() {
      return String.valueOf(this.getBannedPercent());
   }

   public synchronized int getRequest() {
      return this.filerequest;
   }

   public int getRequestPercent() {
      float total = (float)this.networkStatCollection.getRequest();
      return total <= 0.0F ? 0 : (int)((float)this.getRequest() / total * 100.0F);
   }

   public String getRequestPercentString() {
      return String.valueOf(this.getRequestPercent());
   }

   public synchronized long getDownload() {
      return this.download;
   }

   public int getDownloadPercent() {
      float total = (float)this.networkStatCollection.getDownload();
      return total <= 0.0F ? 0 : (int)((float)this.getDownload() / total * 100.0F);
   }

   public String getDownloadPercentString() {
      return String.valueOf(this.getDownloadPercent());
   }

   public float getDownloadRate() {
      float uptime = (float)this.networkStatCollection.getUptime();
      return uptime <= 0.0F ? 0.0F : (float)this.getDownload() / uptime / 1024.0F;
   }

   public String getDownloadRateString() {
      return SwissArmy.calcRate(this.getDownloadRate());
   }

   public synchronized long getUpload() {
      return this.upload;
   }

   public int getUploadPercent() {
      float total = (float)this.networkStatCollection.getUpload();
      return total <= 0.0F ? 0 : (int)((float)this.getUpload() / total * 100.0F);
   }

   public String getUploadPercentString() {
      return String.valueOf(this.getUploadPercent());
   }

   public float getUploadRate() {
      float uptime = (float)this.networkStatCollection.getUptime();
      return uptime <= 0.0F ? 0.0F : (float)this.getUpload() / uptime / 1024.0F;
   }

   public String getUploadRateString() {
      return SwissArmy.calcRate(this.getUploadRate());
   }

   public float getRatio() {
      float upload = (float)this.getUpload();
      return upload == 0.0F ? 0.0F : (float)this.getDownload() / upload;
   }

   public String getRatioString() {
      return "1:" + SwissArmy.calcRate(this.getRatio());
   }

   public synchronized String getSeenString() {
      return String.valueOf(this.seen);
   }

   public synchronized String getBannedString() {
      return String.valueOf(this.banned);
   }

   public synchronized String getRequestString() {
      return String.valueOf(this.filerequest);
   }

   public synchronized String getDownloadString() {
      return SwissArmy.calcStringSize(this.download);
   }

   public synchronized String getUploadString() {
      return SwissArmy.calcStringSize(this.upload);
   }

   public void read(MessageBuffer buffer) {
      synchronized (this) {
         this.stringLong = buffer.getString();
         this.stringShort = buffer.getString();
         this.seen = buffer.getInt32();
         this.banned = buffer.getInt32();
         this.filerequest = buffer.getInt32();
         this.download = buffer.getUInt64();
         this.upload = buffer.getUInt64();
      }
   }
}
