package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile31 extends SharedFile25 {
   String[] uids;

   SharedFile31(ICore var1) {
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
   }
}
