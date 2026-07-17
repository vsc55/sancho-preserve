package sancho.view.friends.clientDirectories;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class ClientDirectoriesTableSorter extends GSorter {
   public ClientDirectoriesTableSorter(ClientDirectoriesTableView var1) {
      super(var1);
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      switch (var4) {
         case 0:
            return this.compareStrings((String)var2, (String)var3);
         default:
            return 0;
      }
   }
}
