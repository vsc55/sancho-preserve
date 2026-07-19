package sancho.view.transfer.fileDialog;

import org.eclipse.swt.graphics.Image;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class SubfileItem {
   String name;
   String magic;
   long size;
   int num;

   public SubfileItem(int num, String name, long size, String magic) {
      this.name = name;
      this.size = size;
      this.num = num;
      this.magic = magic;
   }

   public String getName() {
      return this.name == null ? "" : this.name;
   }

   public String getMagic() {
      return this.magic == null ? "" : this.magic;
   }

   public long getSize() {
      return this.size;
   }

   public int getNum() {
      return this.num;
   }

   public String toString() {
      return this.getName() + " (" + this.getSizeString() + ")";
   }

   public String getExtension() {
      int dotIndex = this.getName().lastIndexOf(".");
      return dotIndex != -1 ? this.getName().substring(dotIndex + 1).toLowerCase() : "";
   }

   public synchronized Image getFileTypeImage() {
      return SResources.getFileTypeImage(this.getExtension());
   }

   public String getSizeString() {
      return SwissArmy.calcStringSize(this.size);
   }
}
