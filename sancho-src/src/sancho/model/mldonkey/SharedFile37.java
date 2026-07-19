package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile37 extends SharedFile31 {
   String[] subFileNames;
   long[] subFileSizes;

   SharedFile37(ICore core) {
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
         String[] namesCopy = new String[this.subFileNames.length];
         System.arraycopy(this.subFileNames, 0, namesCopy, 0, this.subFileNames.length);
         return namesCopy;
      }
   }

   public synchronized long[] getSubFileSizes() {
      if (this.subFileSizes == null) {
         return null;
      } else {
         long[] sizesCopy = new long[this.subFileSizes.length];
         System.arraycopy(this.subFileSizes, 0, sizesCopy, 0, this.subFileSizes.length);
         return sizesCopy;
      }
   }
}
