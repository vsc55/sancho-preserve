package sancho.view.transfer.pending;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.GSorter;

public class PendingTableSorter extends GSorter {
   public PendingTableSorter(PendingTableView view) {
      super(view);
   }

   public boolean sortOrder(int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            return false;
         default:
            return true;
      }
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int columnIndex) {
      Client client1 = (Client)object1;
      Client client2 = (Client)object2;
      switch (columnIndex) {
         case 3:
            return this.compareLongs(client1.getUploaded(), client2.getUploaded());
         case 4:
            return this.compareLongs(client1.getDownloaded(), client2.getDownloaded());
         case 5:
            return this.compareInts(client1.getConnectedTime(), client2.getConnectedTime());
         case 6:
            return this.compareAddrs(client1.getAddr(), client2.getAddr());
         case 7:
            return this.compareInts(client1.getPort(), client2.getPort());
         case 8:
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, object1, object2);
         case 9:
            return this.compareClientStates(client1, client2);
      }
   }
}
