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

   public FriendsTableLabelProvider(FriendsTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      Client var3 = (Client)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            if (var3.isConnected()) {
               return SResources.getImage(var3.hasFiles() ? "FriendsButtonSmallPlus" : "tab.friends.buttonSmall");
            }

            return SResources.getImage(var3.hasFiles() ? "FriendsButtonSmallBWPlus" : "FriendsButtonSmallBW");
         case 1:
            return var3.getStateEnum().getImage();
         case 2:
            return var3.getEnumNetwork().getImage();
         case 3:
            return var3.getSoftwareImage();
         case 4:
         case 5:
         case 6:
         case 8:
         default:
            return null;
         case 7:
            return var3.getAddr().getImage();
         case 9:
            return var3.getClientModeEnum().getImage();
         case 10:
            return var3.hasFilesImage();
      }
   }

   public String getColumnText(Object var1, int var2) {
      Client var3 = (Client)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getDetailedClientActivity();
         case 2:
            return var3.getEnumNetwork().getName();
         case 3:
            return var3.getSoftware();
         case 4:
            return var3.getUploadedString();
         case 5:
            return var3.getDownloadedString();
         case 6:
            return var3.getConnectedTimeString();
         case 7:
            return var3.getAddr().toString();
         case 8:
            return String.valueOf(var3.getPort()).intern();
         case 9:
            return var3.getModeString();
         case 10:
            return var3.hasFilesString();
         default:
            return "?";
      }
   }

   public Color getForeground(Object var1, int var2) {
      if (!this.colors) {
         return null;
      } else {
         Client var3 = (Client)var1;
         if (var3.hasFiles()) {
            return this.hasFilesColor;
         } else {
            return var3.isConnected() ? this.connectedColor : this.disconnectedColor;
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
