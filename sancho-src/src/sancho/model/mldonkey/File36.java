package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File36 extends File31 {
   String[] subFileNames;
   long[] subFileSizes;

   File36(ICore var1) {
      super(var1);
   }

   protected void readSubFiles(MessageBuffer var1) {
      int var2 = var1.getUInt16();
      if (var2 > 0) {
         this.subFileNames = new String[var2];
         this.subFileSizes = new long[var2];

         for (int var3 = 0; var3 < var2; var3++) {
            this.subFileNames[var3] = var1.getString();
            this.subFileSizes[var3] = var1.getUInt64();
         }
      }
   }

   public synchronized String[] getSubFileNames() {
      if (this.subFileNames == null) {
         return null;
      } else {
         String[] var1 = new String[this.subFileNames.length];
         System.arraycopy(this.subFileNames, 0, var1, 0, this.subFileNames.length);
         return var1;
      }
   }

   public synchronized long[] getSubFileSizes() {
      if (this.subFileSizes == null) {
         return null;
      } else {
         long[] var1 = new long[this.subFileSizes.length];
         System.arraycopy(this.subFileSizes, 0, var1, 0, this.subFileSizes.length);
         return var1;
      }
   }
}
