package sancho.view.transfer.fileDialog;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class SubfilesTableSorter extends GSorter {
   public SubfilesTableSorter(SubfilesTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      SubfileItem var5 = (SubfileItem)var2;
      SubfileItem var6 = (SubfileItem)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareLongs(var5.getSize(), var6.getSize());
         case 2:
            return this.compareStrings(var5.getMagic(), var6.getMagic());
         default:
            return 0;
      }
   }
}
