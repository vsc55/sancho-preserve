package sancho.view.friends;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Client;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableLabelProvider;

public class FriendsTableLabelProvider extends GTableLabelProvider {
   boolean colors;
   Color hasFilesColor;
   Color connectedColor;
   Color disconnectedColor;

   public FriendsTableLabelProvider(FriendsTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      Client client = (Client)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            if (client.isConnected()) {
               return SResources.getImage(client.hasFiles() ? "FriendsButtonSmallPlus" : "tab.friends.buttonSmall");
            }

            return SResources.getImage(client.hasFiles() ? "FriendsButtonSmallBWPlus" : "FriendsButtonSmallBW");
         case 1:
            return client.getStateEnum().getImage();
         case 2:
            return client.getEnumNetwork().getImage();
         case 3:
            return client.getSoftwareImage();
         case 4:
         case 5:
         case 6:
         case 8:
         default:
            return null;
         case 7:
            return client.getAddr().getImage();
         case 9:
            return client.getClientModeEnum().getImage();
         case 10:
            return client.hasFilesImage();
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      Client client = (Client)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return client.getName();
         case 1:
            return client.getDetailedClientActivity();
         case 2:
            return client.getEnumNetwork().getName();
         case 3:
            return client.getSoftware();
         case 4:
            return client.getUploadedString();
         case 5:
            return client.getDownloadedString();
         case 6:
            return client.getConnectedTimeString();
         case 7:
            return client.getAddr().toString();
         case 8:
            return String.valueOf(client.getPort()).intern();
         case 9:
            return client.getModeString();
         case 10:
            return client.hasFilesString();
         default:
            return "?";
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      if (!this.colors) {
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
      this.colors = PreferenceLoader.loadBoolean("displayTableColors");
      this.connectedColor = PreferenceLoader.loadColor("clientsConnectedColor");
      this.hasFilesColor = PreferenceLoader.loadColor("clientsHasFilesColor");
      this.disconnectedColor = PreferenceLoader.loadColor("clientsDisconnectedColor");
   }
}
