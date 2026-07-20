package sancho.view.transfer.pending;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Client;
import sancho.view.viewer.table.GTableLabelProvider;

public class PendingTableLabelProvider extends GTableLabelProvider {
   public PendingTableLabelProvider(PendingTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      Client client = (Client)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return client.getEnumNetwork().getImage();
         case 1:
            return client.getNameImage();
         case 2:
            return client.getSoftwareImage();
         case 3:
         case 4:
         case 5:
         case 7:
         case 10:
         default:
            return null;
         case 6:
            return client.getAddr().getImage();
         case 8:
            return client.getClientModeEnum().getImage();
         case 9:
            return client.getStateEnum().getImage();
         case 11:
            return client.getSUIImage();
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      Client client = (Client)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return client.getEnumNetwork().getName();
         case 1:
            return client.getName();
         case 2:
            return client.getSoftware();
         case 3:
            return client.getUploadedString();
         case 4:
            return client.getDownloadedString();
         case 5:
            return client.getConnectedTimeString();
         case 6:
            return client.getAddr().toString();
         case 7:
            return String.valueOf(client.getPort());
         case 8:
            return client.getModeString();
         case 9:
            return client.getDetailedClientActivity();
         case 10:
            return client.getUploadFilename();
         case 11:
            return client.getSUIString();
         default:
            return "";
      }
   }
}
