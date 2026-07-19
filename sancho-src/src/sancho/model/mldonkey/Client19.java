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

   public Client19(ICore core) {
      super(core);
   }

   protected String calcClientSoftwareImageString() {
      String software = this.software.toLowerCase();
      if (this.getEnumNetwork() == EnumNetwork.DONKEY) {
         if (software.startsWith("emu") || software.startsWith("em+") || software.startsWith("imp") || software.startsWith("vcd")) {
            return "emule";
         }

         if (software.startsWith("sza")) {
            return "shareaza";
         }

         if (software.startsWith("amu")) {
            return "amule";
         }

         if (software.startsWith("edk")) {
            return "edonkey";
         }

         if (software.startsWith("lph")) {
            return "lphant";
         }

         if (software.startsWith("hyd")) {
            return "hydranode";
         }

         if (software.startsWith("ovr")) {
            return "hybrid";
         }

         if (software.startsWith("cdk")) {
            return "cdonkey";
         }

         if (software.startsWith("lmu") || software.startsWith("xmu")) {
            return "lmule";
         }

         if (software.startsWith("tml") || software.startsWith("nml") || software.startsWith("oml")) {
            return "mldonkey";
         }
      }

      for (int i = 0; i < nameArray.length; i++) {
         if (software.startsWith(nameArray[i])) {
            return nameArray[i];
         }
      }

      if (software.startsWith("windows")) {
         return "microsoft";
      } else {
         return software.startsWith("cachelogic") ? "mainline" : "client_unknown";
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
         int fileNum = this.getStateFileNum();
         File file;
         if (fileNum != -1 && (file = this.core.getFileCollection().getFile(fileNum)) != null) {
            return file.getName();
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

   protected boolean readMore(MessageBuffer buffer) {
      boolean changed = false;
      long previousDownloaded = this.getDownloaded();
      long previousUploaded = this.getUploaded();
      this.software = buffer.getString();
      this.downloaded = buffer.getUInt64();
      this.uploaded = buffer.getUInt64();
      this.uploadFilename = buffer.getString();
      this.clientSoftwareImageString = this.calcClientSoftwareImageString().intern();
      this.isUploader = !this.uploadFilename.equals("");
      if (previousDownloaded != this.downloaded || previousUploaded != this.uploaded) {
         changed = true;
      }

      return changed;
   }
}
