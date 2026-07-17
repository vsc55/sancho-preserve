package sancho.view.statistics.networks;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Network;
import sancho.view.viewer.GSorter;

public class NetworksTableSorter extends GSorter {
   public NetworksTableSorter(NetworksTableView var1) {
      super(var1);
      this.setDirection(true);
   }

   public boolean sortOrder(int var1) {
      return true;
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Network var5 = (Network)var2;
      Network var6 = (Network)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareLongs(var5.getUploaded(), var6.getUploaded());
         case 2:
            return this.compareLongs(var5.getDownloaded(), var6.getDownloaded());
         default:
            return 0;
      }
   }
}
