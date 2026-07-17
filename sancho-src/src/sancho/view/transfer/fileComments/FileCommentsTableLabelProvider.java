package sancho.view.transfer.fileComments;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.utility.FileComment;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableLabelProvider;

public class FileCommentsTableLabelProvider extends GTableLabelProvider {
   public FileCommentsTableLabelProvider(FileCommentsTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      FileComment var3 = (FileComment)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return SResources.getImage("client");
         case 1:
            return var3.getAddr().getImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      FileComment var3 = (FileComment)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getAddr().toString();
         case 2:
            return var3.getRatingString();
         case 3:
            return var3.getComment();
         default:
            return "";
      }
   }
}
