package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile41 extends SharedFile37 {
   String[] subFileMagics;
   String magic;

   SharedFile41(ICore var1) {
      super(var1);
   }

   protected void readSubFiles(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      if (var2 > 0) {
         this.subFileNames = new String[var2];
         this.subFileSizes = new long[var2];
         this.subFileMagics = new String[var2];

         for (int var3 = 0; var3 < var2; var3++) {
            this.subFileNames[var3] = var1.getString();
            this.subFileSizes[var3] = var1.getUInt64();
            this.subFileMagics[var3] = var1.getString();
         }
      }
   }

   protected void readMagic(MessageBuffer var1) {
      this.magic = var1.getString();
   }

   public synchronized String getMagic() {
      return this.magic != null ? this.magic : "";
   }

   public synchronized String[] getSubFileMagics() {
      if (this.subFileNames == null) {
         return null;
      } else {
         String[] var1 = new String[this.subFileMagics.length];
         System.arraycopy(this.subFileMagics, 0, var1, 0, this.subFileMagics.length);
         return var1;
      }
   }
}
