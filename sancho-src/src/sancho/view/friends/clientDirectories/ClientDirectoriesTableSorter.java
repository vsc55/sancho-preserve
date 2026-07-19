package sancho.view.friends.clientDirectories;

import org.eclipse.jface.viewers.Viewer;
import sancho.view.viewer.GSorter;

public class ClientDirectoriesTableSorter extends GSorter {
   public ClientDirectoriesTableSorter(ClientDirectoriesTableView view) {
      super(view);
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnId) {
      switch (columnId) {
         case 0:
            return this.compareStrings((String)element1, (String)element2);
         default:
            return 0;
      }
   }
}
