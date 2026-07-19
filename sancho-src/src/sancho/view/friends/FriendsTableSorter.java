package sancho.view.friends;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.GSorter;

public class FriendsTableSorter extends GSorter {
   public FriendsTableSorter(FriendsTableView view) {
      super(view);
   }

   public boolean sortOrder(int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return false;
         default:
            return true;
      }
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnId) {
      Client client1 = (Client)element1;
      Client client2 = (Client)element2;
      switch (columnId) {
         case 1:
            return this.compareClientStates(client1, client2);
         case 2:
         case 3:
         case 9:
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, element1, element2);
         case 4:
            return this.compareLongs(client1.getUploaded(), client2.getUploaded());
         case 5:
            return this.compareLongs(client1.getDownloaded(), client2.getDownloaded());
         case 6:
            return this.compareInts(client1.getConnectedTime(), client2.getConnectedTime());
         case 7:
            return this.compareAddrs(client1.getAddr(), client2.getAddr());
         case 8:
            return this.compareInts(client1.getPort(), client2.getPort());
         case 10:
            return this.compareBooleans(client1.hasFiles(), client2.hasFiles());
      }
   }
}
