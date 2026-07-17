package sancho.view.downloadComplete;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class DownloadCompleteTableSorter extends GSorter {
   public DownloadCompleteTableSorter(DownloadCompleteTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      DownloadCompleteItem var5 = (DownloadCompleteItem)var2;
      DownloadCompleteItem var6 = (DownloadCompleteItem)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareLongs(var5.getSize(), var6.getSize());
         case 2:
            return this.compareStrings(var5.getHash(), var6.getHash());
         case 3:
            return this.compareLongs(var5.getDateLong(), var6.getDateLong());
         default:
            return 0;
      }
   }
}
