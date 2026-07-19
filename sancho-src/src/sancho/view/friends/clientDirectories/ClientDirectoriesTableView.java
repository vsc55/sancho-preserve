package sancho.view.friends.clientDirectories;

import org.eclipse.swt.widgets.Composite;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ClientDirectoriesTableView extends GTableView {
   public static final int DIRECTORY = 0;

   public ClientDirectoriesTableView(ClientDirectoriesViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "clientDirectories";
      this.columnLabels = new String[]{"clientDirectories.directory"};
      this.columnDefaultWidths = new int[]{120};
      this.columnAlignment = new int[]{16384};
      this.tableContentProvider = new ClientDirectoriesTableContentProvider(this);
      this.tableLabelProvider = new ClientDirectoriesTableLabelProvider(this);
      this.gSorter = new ClientDirectoriesTableSorter(this);
      this.tableMenuListener = new ClientDirectoriesTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   public void setInput() {
   }

   public void setInput(Object input) {
      this.sViewer.setInput(input);
   }

   public GTableMenuListener getMenuListener() {
      return this.tableMenuListener;
   }

   public void setFilesView(ClientFilesTableView view) {
      ((ClientDirectoriesTableMenuListener)this.tableMenuListener).setFilesView(view);
      ((ClientDirectoriesTableContentProvider)this.tableContentProvider).setFilesView(view);
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((ClientDirectoriesTableMenuListener)this.tableMenuListener);
   }
}
