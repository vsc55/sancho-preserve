package sancho.view.downloadComplete;

import org.eclipse.swt.graphics.Image;
import sancho.view.viewer.table.GTableLabelProvider;

public class DownloadCompleteTableLabelProvider extends GTableLabelProvider {
   public DownloadCompleteTableLabelProvider(DownloadCompleteTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      DownloadCompleteItem item = (DownloadCompleteItem)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return item.getName();
         case 1:
            return item.getSizeString();
         case 2:
            return item.getHash();
         case 3:
            return item.getDateString();
         default:
            return "";
      }
   }
}
