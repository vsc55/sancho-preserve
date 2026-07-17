package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Result27 extends Result25 {
   String[] uids;

   Result27(ICore var1) {
      super(var1);
   }

   public String getMD4() {
      if (this.md4 == null) {
         for (int var1 = 0; var1 < this.uids.length; var1++) {
            if (this.uids[var1].startsWith("urn:ed2k:")) {
               return this.uids[var1].substring(9).intern();
            }
         }
      }

      return super.getMD4();
   }

   public synchronized String[] getUIDs() {
      if (this.uids == null) {
         return null;
      } else {
         String[] var1 = new String[this.uids.length];

         for (int var2 = 0; var2 < var1.length; var2++) {
            var1[var2] = this.uids[var2];
         }

         return var1;
      }
   }

   protected void readUIDs(MessageBuffer var1) {
      this.uids = var1.getStringList();
      if (this.getEnumNetwork() == EnumNetwork.UNKNOWN) {
         for (int var2 = 0; var2 < this.uids.length; var2++) {
            if (this.uids[var2].startsWith("urn:bitprint")) {
               this.networkEnum = EnumNetwork.GNUT;
            } else if (this.uids[var2].startsWith("urn:ed2k")) {
               this.networkEnum = EnumNetwork.DONKEY;
            } else if (this.uids[var2].startsWith("urn:sig2dat")) {
               this.networkEnum = EnumNetwork.FT;
            } else if (this.uids[var2].startsWith("urn:sha1")) {
               this.networkEnum = EnumNetwork.GNUT;
            }
         }
      }
   }

   public void read(int var1, MessageBuffer var2) {
      super.read(var1, var2);
      int var3 = var2.getInt32();
   }
}
