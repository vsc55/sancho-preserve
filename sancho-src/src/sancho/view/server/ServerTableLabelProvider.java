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

   public ServerTableLabelProvider(ServerTableView tableView) {
      super(tableView);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      Server server = (Server)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return server.getNetworkImage();
         case 3:
            return server.getAddr().getImage();
         case 9:
            return server.getPreferredImage();
         case 10:
            return server.getHighLowImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      Server server = (Server)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return server.getNetworkName();
         case 1:
            return server.getName();
         case 2:
            return server.getDescription();
         case 3:
            return server.getAddr().toString();
         case 4:
            return String.valueOf(server.getPort());
         case 5:
            return String.valueOf(server.getScore());
         case 6:
            return server.getNumUsersString();
         case 7:
            return server.getNumFilesString();
         case 8:
            return server.getStateString();
         case 9:
            return server.getPreferredString();
         case 10:
            return server.getHighLowIDString();
         case 11:
            return server.getVersion();
         case 12:
            return server.getMaxUsersString();
         case 13:
            return server.getLowIDUsersString();
         case 14:
            return server.getSoftLimitString();
         case 15:
            return server.getHardLimitString();
         case 16:
            return server.getPingString();
         default:
            return "";
      }
   }

   public Color getForeground(Object element, int columnIndex) {
      if (!this.colors) {
         return null;
      } else {
         Server server = (Server)element;
         if (server.getAddr().isBlocked()) {
            return this.addrBlockedColor;
         } else if (server.isConnected()) {
            return this.connectedColor;
         } else if (server.getStateEnum() == EnumHostState.CONNECTING) {
            return this.connectingColor;
         } else {
            return server.getStateEnum() == EnumHostState.NOT_CONNECTED ? this.disconnectColor : null;
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
