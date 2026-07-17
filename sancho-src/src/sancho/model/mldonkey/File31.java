package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File31 extends File25 {
   String[] uids;

   File31(ICore var1) {
      super(var1);
   }

   public synchronized String getHash() {
      if (this.uids.length > 0) {
         String var1 = this.uids[0];
         int var2 = var1.indexOf(":");
         int var3 = var1.indexOf(":", var2 + 1);
         if (var3 != -1) {
            return var1.substring(var3 + 1);
         }
      }

      return super.getHash();
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
   }
}
