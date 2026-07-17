package sancho.view.transfer.pending;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.GSorter;

public class PendingTableSorter extends GSorter {
   public PendingTableSorter(PendingTableView var1) {
      super(var1);
   }

   public boolean sortOrder(int var1) {
      switch (this.cViewer.getColumnIDs()[var1]) {
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

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Client var5 = (Client)var2;
      Client var6 = (Client)var3;
      switch (var4) {
         case 3:
            return this.compareLongs(var5.getUploaded(), var6.getUploaded());
         case 4:
            return this.compareLongs(var5.getDownloaded(), var6.getDownloaded());
         case 5:
            return this.compareInts(var5.getConnectedTime(), var6.getConnectedTime());
         case 6:
            return this.compareAddrs(var5.getAddr(), var6.getAddr());
         case 7:
            return this.compareInts(var5.getPort(), var6.getPort());
         case 8:
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
         case 9:
            return this.compareClientStates(var5, var6);
      }
   }
}
