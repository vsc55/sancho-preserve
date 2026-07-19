package sancho.view.server;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.view.server.users.ServerUsersTableView;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class ServerTableView extends GTableView {
   public static final int NETWORK = 0;
   public static final int NAME = 1;
   public static final int DESCRIPTION = 2;
   public static final int ADDRESS = 3;
   public static final int PORT = 4;
   public static final int SCORE = 5;
   public static final int USERS = 6;
   public static final int FILES = 7;
   public static final int STATE = 8;
   public static final int PREFERRED = 9;
   public static final int HIGH_ID = 10;
   public static final int VERSION = 11;
   public static final int MAXUSERS = 12;
   public static final int LOWIDUSERS = 13;
   public static final int SOFTLIMIT = 14;
   public static final int HARDLIMIT = 15;
   public static final int PING = 16;

   public ServerTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "server";
      this.columnLabels = new String[]{
         "servers.network",
         "servers.name",
         "servers.description",
         "servers.address",
         "servers.port",
         "servers.score",
         "servers.users",
         "servers.files",
         "servers.state",
         "servers.preferred",
         "servers.id",
         "servers.version",
         "servers.maxUsers",
         "servers.lowIDUsers",
         "servers.softLimit",
         "servers.hardLimit",
         "servers.ping"
      };
      this.columnDefaultWidths = new int[]{70, 160, 160, 120, 50, 55, 55, 60, 80, 50, 50, 50, 50, 50, 50, 50, 50};
      this.columnAlignment = new int[]{
         16384, 16384, 16384, 131072, 131072, 131072, 131072, 131072, 16384, 16384, 16384, 16384, 131072, 131072, 131072, 131072, 131072
      };
      this.validStates = new AbstractEnum[]{
         EnumHostState.BLACKLISTED, EnumHostState.CONNECTED, EnumHostState.CONNECTED_INITIATING, EnumHostState.CONNECTING, EnumHostState.NOT_CONNECTED
      };
      this.tableContentProvider = new ServerTableContentProvider(this);
      this.tableLabelProvider = new ServerTableLabelProvider(this);
      this.gSorter = new ServerTableSorter(this);
      this.tableMenuListener = new ServerTableMenuListener(this);
      this.saveStateFilters = true;
      this.saveNetworkFilters = true;
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addDoubleClickListener((ServerTableMenuListener)this.tableMenuListener);
      this.sViewer.addSelectionChangedListener((ServerTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         this.sViewer.setInput(this.getCore().getServerCollection());
      }
   }

   public void setServerUsersTableView(ServerUsersTableView serverUsersTableView) {
      ((ServerTableMenuListener)this.tableMenuListener).setServerUsersTableView(serverUsersTableView);
   }
}
