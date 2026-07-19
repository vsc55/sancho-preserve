package sancho.view.transfer.fileDialog;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class SubfilesTableSorter extends GSorter {
   public SubfilesTableSorter(SubfilesTableView view) {
      super(view);
      this.setDirection(true);
   }

   public boolean sortOrder(int columnID) {
      return true;
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnID) {
      SubfileItem subfile1 = (SubfileItem)element1;
      SubfileItem subfile2 = (SubfileItem)element2;
      switch (columnID) {
         case 0:
            return this.compareStrings(subfile1.getName(), subfile2.getName());
         case 1:
            return this.compareLongs(subfile1.getSize(), subfile2.getSize());
         case 2:
            return this.compareStrings(subfile1.getMagic(), subfile2.getMagic());
         default:
            return 0;
      }
   }
}
