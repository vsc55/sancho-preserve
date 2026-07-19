package sancho.view.transfer.fileComments;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.utility.FileComment;
import sancho.view.viewer.GSorter;

public class FileCommentsTableSorter extends GSorter {
   public FileCommentsTableSorter(FileCommentsTableView view) {
      super(view);
      this.setDirection(true);
   }

   public boolean sortOrder(int columnIndex) {
      return true;
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int columnIndex) {
      FileComment comment1 = (FileComment)object1;
      FileComment comment2 = (FileComment)object2;
      switch (columnIndex) {
         case 0:
            return this.compareStrings(comment1.getName(), comment2.getName());
         case 1:
            return this.compareAddrs(comment1.getAddr(), comment2.getAddr());
         case 2:
            return this.compareInts(comment1.getRating(), comment2.getRating());
         case 3:
            return this.compareStrings(comment1.getComment(), comment2.getComment());
         default:
            return 0;
      }
   }
}
