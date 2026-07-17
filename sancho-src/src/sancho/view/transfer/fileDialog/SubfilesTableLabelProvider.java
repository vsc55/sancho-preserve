package sancho.view.transfer.fileDialog;

import org.eclipse.swt.graphics.Image;
import sancho.view.viewer.table.GTableLabelProvider;

public class SubfilesTableLabelProvider extends GTableLabelProvider {
   public SubfilesTableLabelProvider(SubfilesTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      SubfileItem var3 = (SubfileItem)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getFileTypeImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      SubfileItem var3 = (SubfileItem)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getSizeString();
         case 2:
            return var3.getMagic();
         default:
            return "";
      }
   }
}
