package sancho.view.transfer.clients;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public class ClientTableLabelProvider extends GTableLabelProvider {
   boolean displayColors;
   Color hasFilesColor;
   Color connectedColor;
   Color disconnectedColor;

   public ClientTableLabelProvider(ClientTableView view) {
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
         case 11:
         default:
            return null;
         case 6:
            return client.getAddr().getImage();
         case 8:
            return client.getClientModeEnum().getImage();
         case 9:
            return client.getStateEnum().getImage();
         case 10:
            return client.hasFilesImage();
         case 12:
            return client.getSUIImage();
         case 13:
            return client.getAddr().getImage();
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
            return client.hasFilesString();
         case 11:
            File file = (File)this.gView.getViewer().getInput();
            if (file instanceof File) {
               return client.getFileAvailabilityPercentString(file);
            }

            return "";
         case 12:
            return client.getSUIString();
         case 13:
            return client.getAddr().getCountry();
         default:
            return "";
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      if (!this.displayColors) {
         return null;
      } else {
         Client client = (Client)element;
         if (client.hasFiles()) {
            return this.hasFilesColor;
         } else {
            return client.isConnected() ? this.connectedColor : this.disconnectedColor;
         }
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.displayColors = PreferenceLoader.loadBoolean("displayTableColors");
      this.connectedColor = PreferenceLoader.loadColor("clientsConnectedColor");
      this.hasFilesColor = PreferenceLoader.loadColor("clientsHasFilesColor");
      this.disconnectedColor = PreferenceLoader.loadColor("clientsDisconnectedColor");
   }
}
