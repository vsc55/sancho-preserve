package sancho.view.shares;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class UploadTableView extends GTableView {
   public static final int NETWORK = 0;
   public static final int BYTES = 1;
   public static final int REQUESTS = 2;
   public static final int NAME = 3;
   public static final int SIZE = 4;
   public static final int MAGIC = 5;

   public UploadTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "upload";
      this.columnLabels = new String[]{"upload.network", "upload.uploaded", "upload.requests", "upload.name", "upload.size", "upload.magic"};
      this.columnDefaultWidths = new int[]{100, 100, 100, 250, 100, 250};
      this.columnAlignment = new int[]{16384, 131072, 131072, 16384, 131072, 16384};
      this.gSorter = new UploadTableSorter(this);
      this.tableContentProvider = new UploadTableContentProvider(this);
      this.tableLabelProvider = new UploadTableLabelProvider(this);
      this.tableMenuListener = new UploadTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((UploadTableMenuListener)this.tableMenuListener);
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         this.sViewer.setInput(this.getCore().getSharedFileCollection());
      }
   }
}
