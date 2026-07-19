package sancho.view.statistics.networks;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Network;
import sancho.view.viewer.GSorter;

public class NetworksTableSorter extends GSorter {
   public NetworksTableSorter(NetworksTableView view) {
      super(view);
      this.setDirection(true);
   }

   public boolean sortOrder(int id) {
      return true;
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int id) {
      Network network1 = (Network)object1;
      Network network2 = (Network)object2;
      switch (id) {
         case 0:
            return this.compareStrings(network1.getName(), network2.getName());
         case 1:
            return this.compareLongs(network1.getUploaded(), network2.getUploaded());
         case 2:
            return this.compareLongs(network1.getDownloaded(), network2.getDownloaded());
         default:
            return 0;
      }
   }
}
