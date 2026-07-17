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

   NetworkStat(NetworkStatCollection var1) {
      this.networkStatCollection = var1;
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
      float var1 = (float)this.networkStatCollection.getSeen();
      return var1 <= 0.0F ? 0 : (int)((float)this.getSeen() / var1 * 100.0F);
   }

   public String getSeenPercentString() {
      return String.valueOf(this.getSeenPercent());
   }

   public synchronized int getBanned() {
      return this.banned;
   }

   public int getBannedPercent() {
      float var1 = (float)this.networkStatCollection.getBanned();
      return var1 <= 0.0F ? 0 : (int)((float)this.getBanned() / var1 * 100.0F);
   }

   public String getBannedPercentString() {
      return String.valueOf(this.getBannedPercent());
   }

   public synchronized int getRequest() {
      return this.filerequest;
   }

   public int getRequestPercent() {
      float var1 = (float)this.networkStatCollection.getRequest();
      return var1 <= 0.0F ? 0 : (int)((float)this.getRequest() / var1 * 100.0F);
   }

   public String getRequestPercentString() {
      return String.valueOf(this.getRequestPercent());
   }

   public synchronized long getDownload() {
      return this.download;
   }

   public int getDownloadPercent() {
      float var1 = (float)this.networkStatCollection.getDownload();
      return var1 <= 0.0F ? 0 : (int)((float)this.getDownload() / var1 * 100.0F);
   }

   public String getDownloadPercentString() {
      return String.valueOf(this.getDownloadPercent());
   }

   public float getDownloadRate() {
      float var1 = (float)this.networkStatCollection.getUptime();
      return var1 <= 0.0F ? 0.0F : (float)this.getDownload() / var1 / 1024.0F;
   }

   public String getDownloadRateString() {
      return SwissArmy.calcRate(this.getDownloadRate());
   }

   public synchronized long getUpload() {
      return this.upload;
   }

   public int getUploadPercent() {
      float var1 = (float)this.networkStatCollection.getUpload();
      return var1 <= 0.0F ? 0 : (int)((float)this.getUpload() / var1 * 100.0F);
   }

   public String getUploadPercentString() {
      return String.valueOf(this.getUploadPercent());
   }

   public float getUploadRate() {
      float var1 = (float)this.networkStatCollection.getUptime();
      return var1 <= 0.0F ? 0.0F : (float)this.getUpload() / var1 / 1024.0F;
   }

   public String getUploadRateString() {
      return SwissArmy.calcRate(this.getUploadRate());
   }

   public float getRatio() {
      float var1 = (float)this.getUpload();
      return var1 == 0.0F ? 0.0F : (float)this.getDownload() / var1;
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

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.stringLong = var1.getString();
         this.stringShort = var1.getString();
         this.seen = var1.getInt32();
         this.banned = var1.getInt32();
         this.filerequest = var1.getInt32();
         this.download = var1.getUInt64();
         this.upload = var1.getUInt64();
      }
   }
}
