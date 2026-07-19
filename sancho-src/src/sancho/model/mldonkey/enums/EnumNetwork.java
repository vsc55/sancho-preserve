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

   private EnumNetwork(int value, String name) {
      super(value, "e.network." + name);
      this.resName = "e.network." + name;
   }

   public Image getImage() {
      return this.getImage("connected-16");
   }

   public ImageDescriptor getImageDescriptor() {
      return this.getImageDescriptor("connected-16");
   }

   public ImageDescriptor getImageDescriptor(String imageName) {
      return this == OV ? SResources.getImageDescriptor("e.network.donkey." + imageName) : SResources.getImageDescriptor(this.resName + "." + imageName);
   }

   public Image getImage(String imageName) {
      return this == OV ? SResources.getImage("e.network.donkey." + imageName) : SResources.getImage(this.resName + "." + imageName);
   }

   public String getImageString() {
      return this == OV ? "e.network.donkey.connected-16" : this.resName + ".connected-16";
   }

   public static EnumNetwork stringToEnum(String name) {
      if (name.equalsIgnoreCase("Donkey")) {
         return DONKEY;
      } else if (name.equalsIgnoreCase("Fasttrack")) {
         return FT;
      } else if (name.equalsIgnoreCase("Soulseek")) {
         return SOULSEEK;
      } else if (name.equalsIgnoreCase("BitTorrent")) {
         return BT;
      } else if (name.equalsIgnoreCase("Overnet")) {
         return OV;
      } else if (name.equalsIgnoreCase("Gnutella")) {
         return GNUT;
      } else if (name.equalsIgnoreCase("Gnutella2") || name.equalsIgnoreCase("G2")) {
         return GNUT2;
      } else if (name.equalsIgnoreCase("Direct Connect")) {
         return DC;
      } else if (name.equalsIgnoreCase("Open Napster") || name.equalsIgnoreCase("OpenNapster")) {
         return OPENNP;
      } else if (name.equalsIgnoreCase("MultiNet")) {
         return MULTINET;
      } else {
         return name.equalsIgnoreCase("FileTP") ? FILETP : UNKNOWN;
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
