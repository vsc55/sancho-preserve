package sancho.view.shares;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.SharedFile;
import sancho.view.viewer.table.GTableLabelProvider;

public class UploadTableLabelProvider extends GTableLabelProvider {
   public UploadTableLabelProvider(UploadTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      SharedFile var3 = (SharedFile)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNetworkImage();
         case 3:
            return var3.getFileTypeImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      SharedFile var3 = (SharedFile)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNetworkName();
         case 1:
            return var3.getUploadedString();
         case 2:
            return var3.getRequestsString();
         case 3:
            return var3.getName();
         case 4:
            return var3.getSizeString();
         case 5:
            return var3.getMagic();
         default:
            return "";
      }
   }
}
