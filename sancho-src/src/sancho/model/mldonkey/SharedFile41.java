package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile41 extends SharedFile37 {
   String[] subFileMagics;
   String magic;

   SharedFile41(ICore core) {
      super(core);
   }

   protected void readSubFiles(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (count > 0) {
         this.subFileNames = new String[count];
         this.subFileSizes = new long[count];
         this.subFileMagics = new String[count];

         for (int i = 0; i < count; i++) {
            this.subFileNames[i] = buffer.getString();
            this.subFileSizes[i] = buffer.getUInt64();
            this.subFileMagics[i] = buffer.getString();
         }
      }
   }

   protected void readMagic(MessageBuffer buffer) {
      this.magic = buffer.getString();
   }

   public synchronized String getMagic() {
      return this.magic != null ? this.magic : "";
   }

   public synchronized String[] getSubFileMagics() {
      if (this.subFileNames == null) {
         return null;
      } else {
         String[] magicsCopy = new String[this.subFileMagics.length];
         System.arraycopy(this.subFileMagics, 0, magicsCopy, 0, this.subFileMagics.length);
         return magicsCopy;
      }
   }
}
