package sancho.view.transfer.fileComments;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.utility.FileComment;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableLabelProvider;

public class FileCommentsTableLabelProvider extends GTableLabelProvider {
   public FileCommentsTableLabelProvider(FileCommentsTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      FileComment comment = (FileComment)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return SResources.getImage("client");
         case 1:
            return comment.getAddr().getImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      FileComment comment = (FileComment)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return comment.getName();
         case 1:
            return comment.getAddr().toString();
         case 2:
            return comment.getRatingString();
         case 3:
            return comment.getComment();
         default:
            return "";
      }
   }
}
