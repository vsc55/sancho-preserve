package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Result27 extends Result25 {
   String[] uids;

   Result27(ICore core) {
      super(core);
   }

   public String getMD4() {
      if (this.md4 == null) {
         for (int i = 0; i < this.uids.length; i++) {
            if (this.uids[i].startsWith("urn:ed2k:")) {
               return this.uids[i].substring(9);
            }
         }
      }

      return super.getMD4();
   }

   public synchronized String[] getUIDs() {
      if (this.uids == null) {
         return null;
      } else {
         String[] uids = new String[this.uids.length];

         for (int i = 0; i < uids.length; i++) {
            uids[i] = this.uids[i];
         }

         return uids;
      }
   }

   protected void readUIDs(MessageBuffer buffer) {
      this.uids = buffer.getStringList();
      if (this.getEnumNetwork() == EnumNetwork.UNKNOWN) {
         for (int i = 0; i < this.uids.length; i++) {
            if (this.uids[i].startsWith("urn:bitprint")) {
               this.networkEnum = EnumNetwork.GNUT;
            } else if (this.uids[i].startsWith("urn:ed2k")) {
               this.networkEnum = EnumNetwork.DONKEY;
            } else if (this.uids[i].startsWith("urn:sig2dat")) {
               this.networkEnum = EnumNetwork.FT;
            } else if (this.uids[i].startsWith("urn:sha1")) {
               this.networkEnum = EnumNetwork.GNUT;
            }
         }
      }
   }

   public void read(int id, MessageBuffer buffer) {
      super.read(id, buffer);
      int unused = buffer.getInt32();
   }
}
