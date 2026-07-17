package sancho.model.mldonkey;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class ClientStats extends AObjectO {
   private static final String S_TCP = "TCP: ";
   private static final String S_UDP = " | UDP: ";
   private static final String S_TOTAL = " | Total: ";
   private static final String S_KBS = " KB/s";
   protected Network[] connectedNetworks;
   protected long downloadCounter;
   protected int numDownloadedFiles;
   protected int numDownloadingFiles;
   protected int numSharedFiles;
   protected long sharedCounter;
   protected float tcpDownloadRate;
   protected float tcpUploadRate;
   protected float udpDownloadRate;
   protected float udpUploadRate;
   protected long uploadCounter;
   public static final DecimalFormat df000 = new DecimalFormat("0.00");
   public static final FieldPosition FP = new FieldPosition(1);

   ClientStats(ICore var1) {
      super(var1);
   }

   public synchronized long getNumDownloadedFiles() {
      return (long)this.numDownloadedFiles;
   }

   public synchronized long getSharedCounter() {
      return this.sharedCounter;
   }

   public synchronized int getNumDownloadingFiles() {
      return this.numDownloadingFiles;
   }

   public synchronized long getDownloadCounter() {
      return this.downloadCounter;
   }

   public synchronized int getNumSharedFiles() {
      return this.numSharedFiles;
   }

   public synchronized float getTcpDownloadRate() {
      return this.tcpDownloadRate;
   }

   public String getTcpDownRateString() {
      return toKBs(this.getTcpDownloadRate(), false);
   }

   public String getTcpDownRateStringS() {
      return toKBs(this.getTcpDownloadRate(), true);
   }

   public synchronized float getTcpUploadRate() {
      return this.tcpUploadRate;
   }

   public String getDownloadToolTip() {
      StringBuffer var1 = new StringBuffer(64);
      var1.append("TCP: ");
      var1.append(this.getTcpDownRateString());
      var1.append(" | UDP: ");
      var1.append(this.getUdpDownRateString());
      var1.append(" | Total: ");
      var1.append(this.getTotalDownRateString());
      return var1.toString();
   }

   public String getUploadToolTip() {
      StringBuffer var1 = new StringBuffer(64);
      var1.append("TCP: ");
      var1.append(this.getTcpUpRateString());
      var1.append(" | UDP: ");
      var1.append(this.getUdpUpRateString());
      var1.append(" | Total: ");
      var1.append(this.getTotalUpRateString());
      return var1.toString();
   }

   public String getTcpUpRateString() {
      return toKBs(this.getTcpUploadRate(), false);
   }

   public String getTcpUpRateStringS() {
      return toKBs(this.getTcpUploadRate(), true);
   }

   public String getTotalDownRateString() {
      return toKBs(this.getTcpDownloadRate() + this.getUdpDownloadRate(), false);
   }

   public String getTotalUpRateString() {
      return toKBs(this.getTcpUploadRate() + this.getUdpUploadRate(), false);
   }

   public synchronized float getUdpDownloadRate() {
      return this.udpDownloadRate;
   }

   public String getUdpDownRateString() {
      return toKBs(this.getUdpDownloadRate(), false);
   }

   public synchronized float getUdpUploadRate() {
      return this.udpUploadRate;
   }

   public String getUdpUpRateString() {
      return toKBs(this.getUdpUploadRate(), false);
   }

   public synchronized long getUploadCounter() {
      return this.uploadCounter;
   }

   public synchronized void read(MessageBuffer var1) {
      this.uploadCounter = var1.getUInt64();
      this.downloadCounter = var1.getUInt64();
      this.sharedCounter = var1.getUInt64();
      this.numSharedFiles = var1.getInt32();
      this.tcpUploadRate = (float)var1.getInt32() / 1024.0F;
      this.tcpDownloadRate = (float)var1.getInt32() / 1024.0F;
      this.udpUploadRate = (float)var1.getInt32() / 1024.0F;
      this.udpDownloadRate = (float)var1.getInt32() / 1024.0F;
      this.numDownloadingFiles = var1.getInt32();
      this.numDownloadedFiles = var1.getInt32();
      this.readNetworks(var1);
      this.setChanged();
      this.notifyObservers();
   }

   public void readNetworks(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      if (this.connectedNetworks == null || this.connectedNetworks.length != var2) {
         this.connectedNetworks = new Network[var2];
      }

      for (int var3 = 0; var3 < var2; var3++) {
         this.connectedNetworks[var3] = (Network)this.core.getNetworkCollection().get(var1.getInt32());
      }
   }

   public static String toKBs(float var0, boolean var1) {
      StringBuffer var2 = new StringBuffer();
      df000.format((double)var0, var2, FP);
      if (!var1) {
         var2.append(" KB/s");
      }

      return var2.toString();
   }
}
