package sancho.view.transfer.clients;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumHostState;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class ClientTableView extends GTableView {
   public static final int NETWORK = 0;
   public static final int NAME = 1;
   public static final int SOFTWARE = 2;
   public static final int UPLOADED = 3;
   public static final int DOWNLOADED = 4;
   public static final int CONNECT_TIME = 5;
   public static final int SOCK_ADDR = 6;
   public static final int PORT = 7;
   public static final int KIND = 8;
   public static final int STATE = 9;
   public static final int HAS_FILES = 10;
   public static final int AVAIL = 11;
   public static final int SUI = 12;
   public static final int COUNTRY = 13;

   public ClientTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "client";
      this.columnLabels = new String[]{
         "client.network",
         "client.name",
         "client.software",
         "client.uploaded",
         "client.downloaded",
         "client.connectTime",
         "client.addr",
         "client.port",
         "client.kind",
         "client.state",
         "client.hasFiles",
         "client.availability",
         "client.sui",
         "client.country"
      };
      this.columnAlignment = new int[]{16384, 16384, 16384, 131072, 131072, 131072, 131072, 131072, 16384, 16384, 16384, 131072, 16384, 16384};
      this.columnDefaultWidths = new int[]{100, 100, 75, 75, 75, 75, 100, 75, 75, 150, 75, 75, 75, 100};
      this.validStates = new AbstractEnum[]{
         EnumHostState.BLACKLISTED,
         EnumHostState.CONNECTED,
         EnumHostState.CONNECTED_AND_QUEUED,
         EnumHostState.CONNECTED_DOWNLOADING,
         EnumHostState.CONNECTED_INITIATING,
         EnumHostState.CONNECTING,
         EnumHostState.NEW_HOST,
         EnumHostState.NOT_CONNECTED,
         EnumHostState.NOT_CONNECTED_WAS_QUEUED
      };
      this.gSorter = new ClientTableSorter(this);
      this.tableContentProvider = new ClientTableContentProvider(this, viewFrame.getCLabel());
      this.tableLabelProvider = new ClientTableLabelProvider(this);
      this.tableMenuListener = new ClientTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((ClientTableMenuListener)this.tableMenuListener);
   }

   public void updateDisplay() {
      super.updateDisplay();
      Table table = this.getTable();
      table.setForeground(PreferenceLoader.loadColor("downloadsAvailableColor"));
   }

   public void setInput() {
   }
}
