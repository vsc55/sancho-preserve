package sancho.view.friends.clientFiles;

import org.eclipse.swt.widgets.Composite;
import sancho.view.viewer.table.GTableMenuListener;
import sancho.view.viewer.table.GTableView;

public class ClientFilesTableView extends GTableView {
   public static final int NAME = 0;
   public static final int SIZE = 1;
   public static final int FORMAT = 2;
   public static final int MEDIA = 3;
   public static final int CODEC = 4;
   public static final int BITRATE = 5;
   public static final int LENGTH = 6;
   public static final int HASH = 7;

   public ClientFilesTableView(ClientFilesViewFrame var1) {
      super(var1);
      this.preferenceString = "clientFiles";
      this.columnLabels = new String[]{
         "clientFiles.name",
         "clientFiles.size",
         "clientFiles.format",
         "clientFiles.media",
         "clientFiles.codec",
         "clientFiles.bitrate",
         "clientFiles.length",
         "clientFiles.hash"
      };
      this.columnDefaultWidths = new int[]{150, 65, 50, 50, 60, 60, 60, 90};
      this.columnAlignment = new int[]{16384, 131072, 16384, 16384, 131072, 131072, 131072, 16384};
      this.tableContentProvider = new ClientFilesTableContentProvider(this);
      this.tableLabelProvider = new ClientFilesTableLabelProvider(this);
      this.gSorter = new ClientFilesTableSorter(this);
      this.tableMenuListener = new ClientFilesTableMenuListener(this);
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

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((ClientFilesTableMenuListener)this.tableMenuListener);
   }
}
