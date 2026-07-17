package sancho.model.mldonkey.enums;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import sancho.view.utility.SResources;

public class EnumNetwork extends AbstractEnum {
   public static final EnumNetwork UNKNOWN = new EnumNetwork(0, "unknown");
   public static final EnumNetwork DONKEY = new EnumNetwork(1, "donkey");
   public static final EnumNetwork SOULSEEK = new EnumNetwork(2, "soulseek");
   public static final EnumNetwork GNUT = new EnumNetwork(4, "gnutella");
   public static final EnumNetwork GNUT2 = new EnumNetwork(8, "gnutella2");
   public static final EnumNetwork OV = new EnumNetwork(16, "overnet");
   public static final EnumNetwork BT = new EnumNetwork(32, "bittorrent");
   public static final EnumNetwork FT = new EnumNetwork(64, "fasttrack");
   public static final EnumNetwork OPENNP = new EnumNetwork(128, "opennap");
   public static final EnumNetwork DC = new EnumNetwork(256, "directconnect");
   public static final EnumNetwork MULTINET = new EnumNetwork(512, "multinet");
   public static final EnumNetwork FILETP = new EnumNetwork(1024, "filetp");
   protected String resName;

   private EnumNetwork(int var1, String var2) {
      super(var1, "e.network." + var2);
      this.resName = "e.network." + var2;
   }

   public Image getImage() {
      return this.getImage("connected-16");
   }

   public ImageDescriptor getImageDescriptor() {
      return this.getImageDescriptor("connected-16");
   }

   public ImageDescriptor getImageDescriptor(String var1) {
      return this == OV ? SResources.getImageDescriptor("e.network.donkey." + var1) : SResources.getImageDescriptor(this.resName + "." + var1);
   }

   public Image getImage(String var1) {
      return this == OV ? SResources.getImage("e.network.donkey." + var1) : SResources.getImage(this.resName + "." + var1);
   }

   public String getImageString() {
      return this == OV ? "e.network.donkey.connected-16" : this.resName + ".connected-16";
   }

   public static EnumNetwork stringToEnum(String var0) {
      if (var0.equalsIgnoreCase("Donkey")) {
         return DONKEY;
      } else if (var0.equalsIgnoreCase("Fasttrack")) {
         return FT;
      } else if (var0.equalsIgnoreCase("Soulseek")) {
         return SOULSEEK;
      } else if (var0.equalsIgnoreCase("BitTorrent")) {
         return BT;
      } else if (var0.equalsIgnoreCase("Overnet")) {
         return OV;
      } else if (var0.equalsIgnoreCase("Gnutella")) {
         return GNUT;
      } else if (var0.equalsIgnoreCase("Gnutella2") || var0.equalsIgnoreCase("G2")) {
         return GNUT2;
      } else if (var0.equalsIgnoreCase("Direct Connect")) {
         return DC;
      } else if (var0.equalsIgnoreCase("Open Napster") || var0.equalsIgnoreCase("OpenNapster")) {
         return OPENNP;
      } else if (var0.equalsIgnoreCase("MultiNet")) {
         return MULTINET;
      } else {
         return var0.equalsIgnoreCase("FileTP") ? FILETP : UNKNOWN;
      }
   }

   public String getTempFilePrefix() {
      if (this == BT) {
         return "BT-";
      } else if (this == FT) {
         return "FT-";
      } else if (this == SOULSEEK) {
         return "SK-";
      } else if (this == DC) {
         return "DC-";
      } else if (this == GNUT) {
         return "GNUT-";
      } else if (this == GNUT2) {
         return "GNUT-";
      } else {
         return this == OPENNP ? "ON-" : "";
      }
   }

   public String getDefaultOptionPrefix() {
      if (this == BT) {
         return "BT-";
      } else if (this == FT) {
         return "FT-";
      } else if (this == SOULSEEK) {
         return "SLSK-";
      } else if (this == DC) {
         return "DC-";
      } else if (this == GNUT) {
         return "GNUT-";
      } else if (this == GNUT2) {
         return "G2-";
      } else if (this == OPENNP) {
         return "OpenNap-";
      } else if (this == FILETP) {
         return "FTP-";
      } else {
         return this == DONKEY ? "ED2K-" : "";
      }
   }
}
