package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File36 extends File31 {
   String[] subFileNames;
   long[] subFileSizes;

   File36(ICore core) {
      super(core);
   }

   protected void readSubFiles(MessageBuffer buffer) {
      int count = buffer.getUInt16();
      if (count > 0) {
         this.subFileNames = new String[count];
         this.subFileSizes = new long[count];

         for (int i = 0; i < count; i++) {
            this.subFileNames[i] = buffer.getString();
            this.subFileSizes[i] = buffer.getUInt64();
         }
      }
   }

   public synchronized String[] getSubFileNames() {
      if (this.subFileNames == null) {
         return null;
      } else {
         String[] names = new String[this.subFileNames.length];
         System.arraycopy(this.subFileNames, 0, names, 0, this.subFileNames.length);
         return names;
      }
   }

   public synchronized long[] getSubFileSizes() {
      if (this.subFileSizes == null) {
         return null;
      } else {
         long[] sizes = new long[this.subFileSizes.length];
         System.arraycopy(this.subFileSizes, 0, sizes, 0, this.subFileSizes.length);
         return sizes;
      }
   }
}
