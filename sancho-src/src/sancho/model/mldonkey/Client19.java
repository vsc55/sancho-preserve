package sancho.model.mldonkey;

import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class Client19 extends Client {
   protected static final String S_SZA = "sza";
   protected static final String S_EMU = "emu";
   protected static final String S_EMP = "em+";
   protected static final String S_EDK = "edk";
   protected static final String S_OVR = "ovr";
   protected static final String S_CDK = "cdk";
   protected static final String S_LMU = "lmu";
   protected static final String S_XMU = "xmu";
   protected static final String S_AMU = "amu";
   protected static final String S_TML = "tml";
   protected static final String S_NML = "nml";
   protected static final String S_OML = "oml";
   protected static final String S_LPH = "lph";
   protected static final String S_HYD = "hyd";
   protected static final String S_VCD = "vcd";
   protected static final String S_IMP = "imp";
   protected static final String[] nameArray = new String[]{
      "azureus",
      "abc",
      "bittornado",
      "mainline",
      "g3",
      "torrentstorm",
      "bitcomet",
      "bitpump",
      "bitsonwheels",
      "exeem",
      "bitrocket",
      "mldonkey",
      "opera",
      "shareaza",
      "bitlord",
      "utorrent",
      "lphant",
      "transmission",
      "hydranode",
      "retriever",
      "bitspirit",
      "ktorrent",
      "libtorrent",
      "osprey",
      "limewire",
      "bearshare",
      "360share",
      "xolox",
      "morph",
      "imesh",
      "kazaa",
      "etomi",
      "microsoft",
      "apache",
      "unix",
      "rufus",
      "qbittorrent",
      "xtorrent",
      "deluge",
      "flashget",
      "ares",
      "bittyrant",
      "btuga",
      "foxtorrent",
      "blizzard",
      "tuotu",
      "pando",
      "halite",
      "arctic",
      "avicora",
      "xantorrent",
      "ziptorrent",
      "vagaa",
      "artemis",
      "wyzo",
      "tribler",
      "symtorrent",
      "hurricane"
   };
   protected String clientSoftwareImageString;
   protected long downloaded;
   protected boolean isUploader;
   protected String software;
   protected long uploaded;
   protected String uploadFilename;

   public Client19(ICore var1) {
      super(var1);
   }

   protected String calcClientSoftwareImageString() {
      String var1 = this.software.toLowerCase();
      if (this.getEnumNetwork() == EnumNetwork.DONKEY) {
         if (var1.startsWith("emu") || var1.startsWith("em+") || var1.startsWith("imp") || var1.startsWith("vcd")) {
            return "emule";
         }

         if (var1.startsWith("sza")) {
            return "shareaza";
         }

         if (var1.startsWith("amu")) {
            return "amule";
         }

         if (var1.startsWith("edk")) {
            return "edonkey";
         }

         if (var1.startsWith("lph")) {
            return "lphant";
         }

         if (var1.startsWith("hyd")) {
            return "hydranode";
         }

         if (var1.startsWith("ovr")) {
            return "hybrid";
         }

         if (var1.startsWith("cdk")) {
            return "cdonkey";
         }

         if (var1.startsWith("lmu") || var1.startsWith("xmu")) {
            return "lmule";
         }

         if (var1.startsWith("tml") || var1.startsWith("nml") || var1.startsWith("oml")) {
            return "mldonkey";
         }
      }

      for (int var2 = 0; var2 < nameArray.length; var2++) {
         if (var1.startsWith(nameArray[var2])) {
            return nameArray[var2];
         }
      }

      if (var1.startsWith("windows")) {
         return "microsoft";
      } else {
         return var1.startsWith("cachelogic") ? "mainline" : "client_unknown";
      }
   }

   public synchronized long getDownloaded() {
      return this.downloaded;
   }

   public synchronized String getDownloadedString() {
      return SwissArmy.calcStringSize(this.downloaded);
   }

   public synchronized String getSoftware() {
      return this.software != null ? this.software : "";
   }

   public synchronized Image getSoftwareImage() {
      return SResources.getImage(this.clientSoftwareImageString);
   }

   public synchronized long getUploaded() {
      return this.uploaded;
   }

   public synchronized String getUploadedString() {
      return SwissArmy.calcStringSize(this.uploaded);
   }

   public String getUploadFilename() {
      if (!this.isUploader() && this.getStateEnum() == EnumHostState.CONNECTED_DOWNLOADING) {
         int var1 = this.getStateFileNum();
         File var2;
         if (var1 != -1 && (var2 = this.core.getFileCollection().getFile(var1)) != null) {
            return var2.getName();
         }
      }

      return this.getRealUploadFilename();
   }

   private synchronized String getRealUploadFilename() {
      return this.uploadFilename != null ? this.uploadFilename : "";
   }

   public synchronized boolean isUploader() {
      return this.isUploader;
   }

   protected boolean readMore(MessageBuffer var1) {
      boolean var2 = false;
      long var3 = this.getDownloaded();
      long var5 = this.getUploaded();
      this.software = var1.getString();
      this.downloaded = var1.getUInt64();
      this.uploaded = var1.getUInt64();
      this.uploadFilename = var1.getString();
      this.clientSoftwareImageString = this.calcClientSoftwareImageString().intern();
      this.isUploader = !this.uploadFilename.equals("");
      if (var3 != this.downloaded || var5 != this.uploaded) {
         var2 = true;
      }

      return var2;
   }
}
