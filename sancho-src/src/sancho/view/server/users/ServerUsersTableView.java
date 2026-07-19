package sancho.view.server.users;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.Server;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class ServerUsersTableView extends GTableView {
   public static final int NAME = 0;
   public static final int TAGS = 1;
   public static final int ADDR = 2;
   public static final int PORT = 3;

   public ServerUsersTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "serverUsers";
      this.columnLabels = new String[]{"serverUsers.name", "serverUsers.tags", "serverUsers.addr", "serverUsers.port"};
      this.columnAlignment = new int[]{16384, 16384, 131072, 131072};
      this.columnDefaultWidths = new int[]{100, 100, 75, 75};
      this.gSorter = new ServerUsersTableSorter(this);
      this.tableContentProvider = new ServerUsersTableContentProvider(this, viewFrame.getCLabel());
      this.tableLabelProvider = new ServerUsersTableLabelProvider(this);
      this.tableMenuListener = new ServerUsersTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((ServerUsersTableMenuListener)this.tableMenuListener);
   }

   public void setInput() {
   }

   public void setInput(Server server) {
      this.sViewer.setInput(server);
   }
}
