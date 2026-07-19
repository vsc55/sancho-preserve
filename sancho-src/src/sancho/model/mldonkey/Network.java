package sancho.model.mldonkey;

import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class Network extends AObject {
   private static final String RS_CONNECTED_TO = SResources.getString("sl.n.connectedTo");
   private static final String RS_ENABLED = SResources.getString("sl.n.enabled");
   private static final String RS_DISABLED = SResources.getString("sl.n.disabled");
   private static final String S_CONNECTED = "connected";
   private static final String S_DISABLED = "disabled";
   private static final String S_DISCONNECTED = "disconnected";
   private static final String S_BAD_CONNECT = "badconnect";
   protected String configFile;
   protected long downloaded;
   private boolean enabled;
   private EnumNetwork enumNetwork;
   protected int id;
   protected String name;
   protected long uploaded;

   Network(ICore core) {
      super(core);
   }

   public void getStats() {
   }

   public boolean equals(EnumNetwork enumNetwork) {
      return this.getEnumNetwork() == enumNetwork;
   }

   public boolean equals(Object object) {
      return object instanceof Network && this.getId() == ((Network)object).getId();
   }

   public synchronized String getConfigFile() {
      return this.configFile != null ? this.configFile : "";
   }

   public synchronized long getDownloaded() {
      return this.downloaded;
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.enumNetwork;
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized Image getImage() {
      if (!this.isEnabled()) {
         return this.enumNetwork.getImage("disabled");
      } else if (this.core.getProtocol() >= 18 && !this.isVirtual() && (this.hasServers() || this.hasSupernodes())) {
         int maxConnected = this.core.getOptionCollection().getMaxConnected(this);
         int connectedServers = this.numConnectedServers();
         return (connectedServers < 1 || this.enumNetwork != EnumNetwork.DC) && connectedServers < maxConnected
            ? this.enumNetwork.getImage(connectedServers == 0 ? "disconnected" : "badconnect")
            : this.enumNetwork.getImage("connected");
      } else {
         return this.enumNetwork.getImage("connected");
      }
   }

   public String getName() {
      return this.name != null ? this.name : "";
   }

   public String getToolTip() {
      StringBuffer buffer = new StringBuffer(256);
      if (!this.isEnabled() || !this.hasServers() && !this.hasSupernodes()) {
         if (this.isVirtual()) {
            return this.getName();
         } else {
            buffer.append(this.getName());
            buffer.append(" ");
            buffer.append(this.isEnabled() ? RS_ENABLED : RS_DISABLED);
            return buffer.toString();
         }
      } else {
         buffer.append(this.getName());
         buffer.append(" ");
         buffer.append(RS_CONNECTED_TO);
         buffer.append(this.numConnectedServers());
         return buffer.toString();
      }
   }

   public synchronized long getUploaded() {
      return this.uploaded;
   }

   public synchronized String getUploadedString() {
      return SwissArmy.calcStringSize(this.uploaded);
   }

   public synchronized String getDownloadedString() {
      return SwissArmy.calcStringSize(this.downloaded);
   }

   public synchronized String toString() {
      StringBuffer buffer = new StringBuffer(50);
      buffer.append(this.getName());
      buffer.append(": U:");
      buffer.append(this.getUploadedString());
      buffer.append(" D:");
      buffer.append(this.getDownloadedString());
      buffer.append(" (");
      buffer.append(SResources.getString(this.isEnabled() ? "stats.enabled" : "stats.disabled"));
      buffer.append(")");
      return buffer.toString();
   }

   public synchronized boolean hasChat() {
      return this.enumNetwork == EnumNetwork.DONKEY || this.enumNetwork == EnumNetwork.OV;
   }

   public int hashCode() {
      return this.getId();
   }

   public synchronized boolean hasRooms() {
      return this.enumNetwork == EnumNetwork.SOULSEEK || this.enumNetwork == EnumNetwork.DC;
   }

   public synchronized boolean hasServers() {
      return this.enumNetwork != EnumNetwork.BT && this.enumNetwork != EnumNetwork.FT && this.enumNetwork != EnumNetwork.GNUT;
   }

   public synchronized boolean hasSupernodes() {
      return this.enumNetwork == EnumNetwork.FT || this.enumNetwork == EnumNetwork.GNUT || this.enumNetwork == EnumNetwork.GNUT2;
   }

   public synchronized boolean hasUpload() {
      return this.enumNetwork == EnumNetwork.DONKEY
         || this.enumNetwork == EnumNetwork.OV
         || this.enumNetwork == EnumNetwork.BT
         || this.enumNetwork == EnumNetwork.GNUT
         || this.enumNetwork == EnumNetwork.DC;
   }

   public synchronized boolean isEnabled() {
      return this.enabled;
   }

   public boolean isMultinet() {
      return false;
   }

   public synchronized boolean isSearchable() {
      return this.enumNetwork != EnumNetwork.BT;
   }

   public boolean isVirtual() {
      return false;
   }

   public int numConnectedServers() {
      return this.core.getServerCollection().getConnected(this.getEnumNetwork());
   }

   public void read(int id, MessageBuffer buffer) {
      boolean wasEnabled = this.isEnabled();
      synchronized (this) {
         this.id = id;
         this.name = buffer.getString();
         this.enabled = buffer.getBool();
         this.configFile = buffer.getString();
         this.uploaded = buffer.getUInt64();
         this.downloaded = buffer.getUInt64();
         this.enumNetwork = EnumNetwork.stringToEnum(this.name);
      }

      if (!this.isEnabled() && wasEnabled) {
         this.core.getServerCollection().removeAll(this.getEnumNetwork());
      }
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   protected void setConnectedServers(int connectedServers) {
   }

   public NetworkStatCollection[] getNetworkStatCollection() {
      return null;
   }

   public void toggleEnabled() {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), Byte.valueOf((byte)(this.isEnabled() ? 0 : 1))};
      this.core.send((short)40, args);
   }

   public void readStats(MessageBuffer buffer) {
   }
}
