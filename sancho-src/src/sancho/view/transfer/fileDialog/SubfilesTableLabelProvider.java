package sancho.view.transfer.fileDialog;

import org.eclipse.swt.graphics.Image;
import sancho.view.viewer.table.GTableLabelProvider;

public class SubfilesTableLabelProvider extends GTableLabelProvider {
   public SubfilesTableLabelProvider(SubfilesTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      SubfileItem subfile = (SubfileItem)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return subfile.getFileTypeImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      SubfileItem subfile = (SubfileItem)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return subfile.getName();
         case 1:
            return subfile.getSizeString();
         case 2:
            return subfile.getMagic();
         default:
            return "";
      }
   }
}
