package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File31 extends File25 {
   String[] uids;

   File31(ICore core) {
      super(core);
   }

   public synchronized String getHash() {
      if (this.uids.length > 0) {
         String uid = this.uids[0];
         int firstColon = uid.indexOf(":");
         int secondColon = uid.indexOf(":", firstColon + 1);
         if (secondColon != -1) {
            return uid.substring(secondColon + 1);
         }
      }

      return super.getHash();
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
         String[] uidsCopy = new String[this.uids.length];

         for (int i = 0; i < uidsCopy.length; i++) {
            uidsCopy[i] = this.uids[i];
         }

         return uidsCopy;
      }
   }

   protected void readUIDs(MessageBuffer buffer) {
      this.uids = buffer.getStringList();
   }
}
