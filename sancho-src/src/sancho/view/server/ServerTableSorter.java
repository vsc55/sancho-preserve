package sancho.view.server;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.GSorter;

public class ServerTableSorter extends GSorter {
   public ServerTableSorter(ServerTableView var1) {
      super(var1);
   }

   public boolean sortOrder(int var1) {
      switch (this.cViewer.getColumnIDs()[var1]) {
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

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Server var5 = (Server)var2;
      Server var6 = (Server)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getNetworkName(), var6.getNetworkName());
         case 1:
            return this.compareStrings(var5.getName(), var6.getName());
         case 2:
            return this.compareStrings(var5.getDescription(), var6.getDescription());
         case 3:
            return this.compareAddrs(var5.getAddr(), var6.getAddr());
         case 4:
            return this.compareInts(var5.getPort(), var6.getPort());
         case 5:
            return this.compareInts(var5.getScore(), var6.getScore());
         case 6:
            return this.compareLongs(var5.getNumUsers(), var6.getNumUsers());
         case 7:
            return this.compareLongs(var5.getNumFiles(), var6.getNumFiles());
         case 8:
            return this.compareStrings(var5.getStateString(), var6.getStateString());
         case 9:
            return this.compareStrings(var5.getPreferredString(), var6.getPreferredString());
         case 10:
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
         case 11:
            return this.compareStrings(var5.getVersion(), var6.getVersion());
         case 12:
            return this.compareLongs(var5.getMaxUsers(), var6.getMaxUsers());
         case 13:
            return this.compareLongs(var5.getLowIDUsers(), var6.getLowIDUsers());
         case 14:
            return this.compareLongs(var5.getSoftLimit(), var6.getSoftLimit());
         case 15:
            return this.compareLongs(var5.getHardLimit(), var6.getHardLimit());
         case 16:
            return this.compareInts(var5.getPing(), var6.getPing());
      }
   }
}
