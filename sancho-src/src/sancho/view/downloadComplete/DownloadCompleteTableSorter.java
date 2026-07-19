package sancho.view.downloadComplete;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class DownloadCompleteTableSorter extends GSorter {
   public DownloadCompleteTableSorter(DownloadCompleteTableView view) {
      super(view);
      this.setDirection(true);
   }

   public boolean sortOrder(int columnId) {
      return true;
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnId) {
      DownloadCompleteItem item1 = (DownloadCompleteItem)element1;
      DownloadCompleteItem item2 = (DownloadCompleteItem)element2;
      switch (columnId) {
         case 0:
            return this.compareStrings(item1.getName(), item2.getName());
         case 1:
            return this.compareLongs(item1.getSize(), item2.getSize());
         case 2:
            return this.compareStrings(item1.getHash(), item2.getHash());
         case 3:
            return this.compareLongs(item1.getDateLong(), item2.getDateLong());
         default:
            return 0;
      }
   }
}
