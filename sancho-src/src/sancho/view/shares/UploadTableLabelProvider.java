package sancho.view.shares;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.SharedFile;
import sancho.view.viewer.table.GTableLabelProvider;

public class UploadTableLabelProvider extends GTableLabelProvider {
   public UploadTableLabelProvider(UploadTableView tableView) {
      super(tableView);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      SharedFile sharedFile = (SharedFile)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return sharedFile.getNetworkImage();
         case 3:
            return sharedFile.getFileTypeImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      SharedFile sharedFile = (SharedFile)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return sharedFile.getNetworkName();
         case 1:
            return sharedFile.getUploadedString();
         case 2:
            return sharedFile.getRequestsString();
         case 3:
            return sharedFile.getName();
         case 4:
            return sharedFile.getSizeString();
         case 5:
            return sharedFile.getMagic();
         default:
            return "";
      }
   }
}
