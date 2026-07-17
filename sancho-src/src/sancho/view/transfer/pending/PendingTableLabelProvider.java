package sancho.view.transfer.pending;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.table.GTableLabelProvider;

public class PendingTableLabelProvider extends GTableLabelProvider {
   public PendingTableLabelProvider(PendingTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      Client var3 = (Client)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getEnumNetwork().getImage();
         case 1:
            return var3.getNameImage();
         case 2:
            return var3.getSoftwareImage();
         case 3:
         case 4:
         case 5:
         case 7:
         case 10:
         default:
            return null;
         case 6:
            return var3.getAddr().getImage();
         case 8:
            return var3.getClientModeEnum().getImage();
         case 9:
            return var3.getStateEnum().getImage();
         case 11:
            return var3.getSUIImage();
      }
   }

   public String getColumnText(Object var1, int var2) {
      Client var3 = (Client)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getEnumNetwork().getName();
         case 1:
            return var3.getName();
         case 2:
            return var3.getSoftware();
         case 3:
            return var3.getUploadedString();
         case 4:
            return var3.getDownloadedString();
         case 5:
            return var3.getConnectedTimeString();
         case 6:
            return var3.getAddr().toString();
         case 7:
            return String.valueOf(var3.getPort()).intern();
         case 8:
            return var3.getModeString();
         case 9:
            return var3.getDetailedClientActivity();
         case 10:
            return var3.getUploadFilename();
         case 11:
            return var3.getSUIString();
         default:
            return "";
      }
   }
}
