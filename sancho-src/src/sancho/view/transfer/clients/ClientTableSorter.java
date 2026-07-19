package sancho.view.transfer.clients;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.view.viewer.GSorter;

public class ClientTableSorter extends GSorter {
   public ClientTableSorter(ClientTableView view) {
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

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnID) {
      Client client1 = (Client)element1;
      Client client2 = (Client)element2;
      switch (columnID) {
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
         case 12:
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, element1, element2);
         case 9:
            return this.compareClientStates(client1, client2);
         case 10:
            return this.compareBooleans(client1.hasFiles(), client2.hasFiles());
         case 11:
            File file = (File)this.gView.getViewer().getInput();
            if (file instanceof File) {
               return this.comparePercents(client1.getFileAvailabilityPercent(file), client2.getFileAvailabilityPercent(file));
            }

            return 0;
         case 13:
            return this.compareStrings(client1.getAddr().getCountry(), client2.getAddr().getCountry());
      }
   }
}
