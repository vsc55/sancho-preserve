package sancho.view.server;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GSorter;

public class ServerTableSorter extends GSorter {
   public ServerTableSorter(ServerTableView tableView) {
      super(tableView);
   }

   public boolean sortOrder(int columnId) {
      switch (this.cViewer.getColumnIDs()[columnId]) {
         case 0:
         case 1:
         case 2:
         case 8:
            return true;
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         default:
            return false;
      }
   }

   protected int _compare(Viewer viewer, Object firstElement, Object secondElement, int columnId) {
      Server firstServer = (Server)firstElement;
      Server secondServer = (Server)secondElement;
      switch (columnId) {
         case 0:
            return this.compareStrings(firstServer.getNetworkName(), secondServer.getNetworkName());
         case 1:
            return this.compareStrings(firstServer.getName(), secondServer.getName());
         case 2:
            return this.compareStrings(firstServer.getDescription(), secondServer.getDescription());
         case 3:
            return this.compareAddrs(firstServer.getAddr(), secondServer.getAddr());
         case 4:
            return this.compareInts(firstServer.getPort(), secondServer.getPort());
         case 5:
            return this.compareInts(firstServer.getScore(), secondServer.getScore());
         case 6:
            return this.compareLongs(firstServer.getNumUsers(), secondServer.getNumUsers());
         case 7:
            return this.compareLongs(firstServer.getNumFiles(), secondServer.getNumFiles());
         case 8:
            return this.compareStrings(firstServer.getStateString(), secondServer.getStateString());
         case 9:
            return this.compareStrings(firstServer.getPreferredString(), secondServer.getPreferredString());
         case 10:
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, firstElement, secondElement);
         case 11:
            return this.compareStrings(firstServer.getVersion(), secondServer.getVersion());
         case 12:
            return this.compareLongs(firstServer.getMaxUsers(), secondServer.getMaxUsers());
         case 13:
            return this.compareLongs(firstServer.getLowIDUsers(), secondServer.getLowIDUsers());
         case 14:
            return this.compareLongs(firstServer.getSoftLimit(), secondServer.getSoftLimit());
         case 15:
            return this.compareLongs(firstServer.getHardLimit(), secondServer.getHardLimit());
         case 16:
            return this.compareInts(firstServer.getPing(), secondServer.getPing());
      }
   }
}
