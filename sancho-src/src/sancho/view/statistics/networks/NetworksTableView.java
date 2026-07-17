package sancho.view.statistics.networks;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class NetworksTableView extends GTableView {
   public static final int NAME = 0;
   public static final int UPLOADED = 1;
   public static final int DOWNLOADED = 2;

   public NetworksTableView(ViewFrame var1) {
      super(var1);
      this.preferenceString = "networks";
      this.columnLabels = new String[]{"networks.name", "networks.uploaded", "networks.downloaded"};
      this.columnAlignment = new int[]{16384, 131072, 131072};
      this.columnDefaultWidths = new int[]{100, 75, 75};
      this.gSorter = new NetworksTableSorter(this);
      this.tableContentProvider = new NetworksTableContentProvider(this);
      this.tableLabelProvider = new NetworksTableLabelProvider(this);
      this.tableMenuListener = new NetworksTableMenuListener(this);
      this.createContents(var1.getChildComposite());
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((NetworksTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
      this.updateHeader();
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         this.sViewer.setInput(this.getCore().getNetworkCollection().getValues());
         this.updateHeader();
      }
   }

   public void updateHeader() {
      this.getViewFrame().updateCLabelText(SResources.getString("l.networks") + ": " + this.getTable().getItemCount());
   }
}
