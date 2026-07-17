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

   public ClientTableLabelProvider(ClientTableView var1) {
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
         case 11:
         default:
            return null;
         case 6:
            return var3.getAddr().getImage();
         case 8:
            return var3.getClientModeEnum().getImage();
         case 9:
            return var3.getStateEnum().getImage();
         case 10:
            return var3.hasFilesImage();
         case 12:
            return var3.getSUIImage();
         case 13:
            return var3.getAddr().getImage();
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
            return var3.hasFilesString();
         case 11:
            File var4 = (File)this.gView.getViewer().getInput();
            if (var4 instanceof File) {
               return var3.getFileAvailabilityPercentString(var4);
            }

            return "";
         case 12:
            return var3.getSUIString();
         case 13:
            return var3.getAddr().getCountry();
         default:
            return "";
      }
   }

   public Color getForeground(Object var1, int var2) {
      if (!this.displayColors) {
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
      this.displayColors = PreferenceLoader.loadBoolean("displayTableColors");
      this.connectedColor = PreferenceLoader.loadColor("clientsConnectedColor");
      this.hasFilesColor = PreferenceLoader.loadColor("clientsHasFilesColor");
      this.disconnectedColor = PreferenceLoader.loadColor("clientsDisconnectedColor");
   }
}
