package sancho.view.shares;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.SharedFile;
import sancho.view.viewer.GSorter;

public class UploadTableSorter extends GSorter {
   public UploadTableSorter(UploadTableView tableView) {
      super(tableView);
   }

   public boolean sortOrder(int columnIndex) {
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 1:
         case 2:
            return false;
         default:
            return true;
      }
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int columnId) {
      if (object1 == null) {
         return 1;
      } else if (object2 == null) {
         return -1;
      } else {
         SharedFile sharedFile1 = (SharedFile)object1;
         SharedFile sharedFile2 = (SharedFile)object2;
         switch (columnId) {
            case 0:
               return this.compareStrings(sharedFile1.getNetworkName(), sharedFile2.getNetworkName());
            case 1:
               return this.compareLongs(sharedFile1.getBytesUploaded(), sharedFile2.getBytesUploaded());
            case 2:
               return this.compareLongs((long)sharedFile1.getRequests(), (long)sharedFile2.getRequests());
            case 3:
               return this.compareStrings(sharedFile1.getName(), sharedFile2.getName());
            case 4:
               return this.compareLongs(sharedFile1.getSize(), sharedFile2.getSize());
            case 5:
               return this.compareStrings(sharedFile1.getMagic(), sharedFile2.getMagic());
            default:
               return 0;
         }
      }
   }
}
