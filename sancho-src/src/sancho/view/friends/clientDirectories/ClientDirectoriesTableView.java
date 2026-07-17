package sancho.view.friends.clientDirectories;

import org.eclipse.swt.widgets.Composite;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ClientDirectoriesTableView extends GTableView {
   public static final int DIRECTORY = 0;

   public ClientDirectoriesTableView(ClientDirectoriesViewFrame var1) {
      super(var1);
      this.preferenceString = "clientDirectories";
      this.columnLabels = new String[]{"clientDirectories.directory"};
      this.columnDefaultWidths = new int[]{120};
      this.columnAlignment = new int[]{16384};
      this.tableContentProvider = new ClientDirectoriesTableContentProvider(this);
      this.tableLabelProvider = new ClientDirectoriesTableLabelProvider(this);
      this.gSorter = new ClientDirectoriesTableSorter(this);
      this.tableMenuListener = new ClientDirectoriesTableMenuListener(this);
      this.createContents(var1.getChildComposite());
   }

   public void setInput() {
   }

   public void setInput(Object var1) {
      this.sViewer.setInput(var1);
   }

   public GTableMenuListener getMenuListener() {
      return this.tableMenuListener;
   }

   public void setFilesView(ClientFilesTableView var1) {
      ((ClientDirectoriesTableMenuListener)this.tableMenuListener).setFilesView(var1);
      ((ClientDirectoriesTableContentProvider)this.tableContentProvider).setFilesView(var1);
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((ClientDirectoriesTableMenuListener)this.tableMenuListener);
   }
}
