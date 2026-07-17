package sancho.view.shares;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.SharedFile;
import sancho.view.viewer.GSorter;

public class UploadTableSorter extends GSorter {
   public UploadTableSorter(UploadTableView var1) {
      super(var1);
   }

   public boolean sortOrder(int var1) {
      switch (this.cViewer.getColumnIDs()[var1]) {
         case 1:
         case 2:
            return false;
         default:
            return true;
      }
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      if (var2 == null) {
         return 1;
      } else if (var3 == null) {
         return -1;
      } else {
         SharedFile var5 = (SharedFile)var2;
         SharedFile var6 = (SharedFile)var3;
         switch (var4) {
            case 0:
               return this.compareStrings(var5.getNetworkName(), var6.getNetworkName());
            case 1:
               return this.compareLongs(var5.getBytesUploaded(), var6.getBytesUploaded());
            case 2:
               return this.compareLongs((long)var5.getRequests(), (long)var6.getRequests());
            case 3:
               return this.compareStrings(var5.getName(), var6.getName());
            case 4:
               return this.compareLongs(var5.getSize(), var6.getSize());
            case 5:
               return this.compareStrings(var5.getMagic(), var6.getMagic());
            default:
               return 0;
         }
      }
   }
}
