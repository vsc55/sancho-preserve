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

   ClientStats(ICore core) {
      super(core);
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
      StringBuffer text = new StringBuffer(64);
      text.append("TCP: ");
      text.append(this.getTcpDownRateString());
      text.append(" | UDP: ");
      text.append(this.getUdpDownRateString());
      text.append(" | Total: ");
      text.append(this.getTotalDownRateString());
      return text.toString();
   }

   public String getUploadToolTip() {
      StringBuffer text = new StringBuffer(64);
      text.append("TCP: ");
      text.append(this.getTcpUpRateString());
      text.append(" | UDP: ");
      text.append(this.getUdpUpRateString());
      text.append(" | Total: ");
      text.append(this.getTotalUpRateString());
      return text.toString();
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

   public synchronized void read(MessageBuffer buffer) {
      this.uploadCounter = buffer.getUInt64();
      this.downloadCounter = buffer.getUInt64();
      this.sharedCounter = buffer.getUInt64();
      this.numSharedFiles = buffer.getInt32();
      this.tcpUploadRate = (float)buffer.getInt32() / 1024.0F;
      this.tcpDownloadRate = (float)buffer.getInt32() / 1024.0F;
      this.udpUploadRate = (float)buffer.getInt32() / 1024.0F;
      this.udpDownloadRate = (float)buffer.getInt32() / 1024.0F;
      this.numDownloadingFiles = buffer.getInt32();
      this.numDownloadedFiles = buffer.getInt32();
      this.readNetworks(buffer);
      this.setChanged();
      this.notifyObservers();
   }

   public void readNetworks(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (this.connectedNetworks == null || this.connectedNetworks.length != count) {
         this.connectedNetworks = new Network[count];
      }

      for (int i = 0; i < count; i++) {
         this.connectedNetworks[i] = (Network)this.core.getNetworkCollection().get(buffer.getInt32());
      }
   }

   public static String toKBs(float rate, boolean hideUnit) {
      StringBuffer text = new StringBuffer();
      df000.format((double)rate, text, FP);
      if (!hideUnit) {
         text.append(" KB/s");
      }

      return text.toString();
   }
}
