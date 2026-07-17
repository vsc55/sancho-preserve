package sancho.view.friends;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.GSorter;

public class FriendsTableSorter extends GSorter {
   public FriendsTableSorter(FriendsTableView var1) {
      super(var1);
   }

   public boolean sortOrder(int var1) {
      switch (this.cViewer.getColumnIDs()[var1]) {
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

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Client var5 = (Client)var2;
      Client var6 = (Client)var3;
      switch (var4) {
         case 1:
            return this.compareClientStates(var5, var6);
         case 2:
         case 3:
         case 9:
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
         case 4:
            return this.compareLongs(var5.getUploaded(), var6.getUploaded());
         case 5:
            return this.compareLongs(var5.getDownloaded(), var6.getDownloaded());
         case 6:
            return this.compareInts(var5.getConnectedTime(), var6.getConnectedTime());
         case 7:
            return this.compareAddrs(var5.getAddr(), var6.getAddr());
         case 8:
            return this.compareInts(var5.getPort(), var6.getPort());
         case 10:
            return this.compareBooleans(var5.hasFiles(), var6.hasFiles());
      }
   }
}
