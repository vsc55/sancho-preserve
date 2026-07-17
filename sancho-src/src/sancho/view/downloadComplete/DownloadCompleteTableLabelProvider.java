package sancho.view.downloadComplete;

import org.eclipse.swt.graphics.Image;
import sancho.view.viewer.table.GTableLabelProvider;

public class DownloadCompleteTableLabelProvider extends GTableLabelProvider {
   public DownloadCompleteTableLabelProvider(DownloadCompleteTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      switch (this.cViewer.getColumnIDs()[var2]) {
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      DownloadCompleteItem var3 = (DownloadCompleteItem)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getSizeString();
         case 2:
            return var3.getHash();
         case 3:
            return var3.getDateString();
         default:
            return "";
      }
   }
}
