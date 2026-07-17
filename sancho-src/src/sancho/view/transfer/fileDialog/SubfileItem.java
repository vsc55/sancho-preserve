package sancho.view.transfer.fileDialog;

import org.eclipse.swt.graphics.Image;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

public class SubfileItem {
   String name;
   String magic;
   long size;
   int num;

   public SubfileItem(int var1, String var2, long var3, String var5) {
      this.name = var2;
      this.size = var3;
      this.num = var1;
      this.magic = var5;
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
      int var1 = this.getName().lastIndexOf(".");
      return var1 != -1 ? this.getName().substring(var1 + 1).toLowerCase() : "";
   }

   public synchronized Image getFileTypeImage() {
      return SResources.getFileTypeImage(this.getExtension());
   }

   public String getSizeString() {
      return SwissArmy.calcStringSize(this.size);
   }
}
