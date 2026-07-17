package sancho.view.server;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Server;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewer.table.GTableLabelProvider;

public class ServerTableLabelProvider extends GTableLabelProvider {
   private boolean colors;
   private Color connectedColor;
   private Color connectingColor;
   private Color disconnectColor;
   private Color addrBlockedColor;

   public ServerTableLabelProvider(ServerTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      Server var3 = (Server)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNetworkImage();
         case 3:
            return var3.getAddr().getImage();
         case 9:
            return var3.getPreferredImage();
         case 10:
            return var3.getHighLowImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      Server var3 = (Server)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getNetworkName();
         case 1:
            return var3.getName();
         case 2:
            return var3.getDescription();
         case 3:
            return var3.getAddr().toString();
         case 4:
            return String.valueOf(var3.getPort()).intern();
         case 5:
            return String.valueOf(var3.getScore()).intern();
         case 6:
            return var3.getNumUsersString();
         case 7:
            return var3.getNumFilesString();
         case 8:
            return var3.getStateString();
         case 9:
            return var3.getPreferredString();
         case 10:
            return var3.getHighLowIDString();
         case 11:
            return var3.getVersion();
         case 12:
            return var3.getMaxUsersString();
         case 13:
            return var3.getLowIDUsersString();
         case 14:
            return var3.getSoftLimitString();
         case 15:
            return var3.getHardLimitString();
         case 16:
            return var3.getPingString();
         default:
            return "";
      }
   }

   public Color getForeground(Object var1, int var2) {
      if (!this.colors) {
         return null;
      } else {
         Server var3 = (Server)var1;
         if (var3.getAddr().isBlocked()) {
            return this.addrBlockedColor;
         } else if (var3.isConnected()) {
            return this.connectedColor;
         } else if (var3.getStateEnum() == EnumHostState.CONNECTING) {
            return this.connectingColor;
         } else {
            return var3.getStateEnum() == EnumHostState.NOT_CONNECTED ? this.disconnectColor : null;
         }
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      this.colors = PreferenceLoader.loadBoolean("displayTableColors");
      this.connectedColor = PreferenceLoader.loadColor("serverConnectedColor");
      this.connectingColor = PreferenceLoader.loadColor("serverConnectingColor");
      this.disconnectColor = PreferenceLoader.loadColor("serverDisconnectedColor");
      this.addrBlockedColor = PreferenceLoader.loadColor("addrBlockedColor");
   }
}
