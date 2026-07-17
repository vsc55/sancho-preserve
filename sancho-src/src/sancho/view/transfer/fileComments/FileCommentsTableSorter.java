package sancho.view.transfer.fileComments;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.utility.FileComment;
import sancho.view.viewer.GSorter;

public class FileCommentsTableSorter extends GSorter {
   public FileCommentsTableSorter(FileCommentsTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      FileComment var5 = (FileComment)var2;
      FileComment var6 = (FileComment)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareAddrs(var5.getAddr(), var6.getAddr());
         case 2:
            return this.compareInts(var5.getRating(), var6.getRating());
         case 3:
            return this.compareStrings(var5.getComment(), var6.getComment());
         default:
            return 0;
      }
   }
}
