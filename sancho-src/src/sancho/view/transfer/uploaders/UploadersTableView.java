package sancho.view.transfer.uploaders;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class UploadersTableView extends GTableView {
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
   public static final int FILENAME = 10;
   public static final int SUI = 11;

   public UploadersTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "uploaders";
      this.columnLabels = new String[]{
         "uploaders.network",
         "uploaders.name",
         "uploaders.software",
         "uploaders.uploaded",
         "uploaders.downloaded",
         "uploaders.connectTime",
         "uploaders.addr",
         "uploaders.port",
         "uploaders.kind",
         "uploaders.state",
         "uploaders.filename",
         "uploaders.sui"
      };
      this.columnDefaultWidths = new int[]{100, 100, 100, 100, 100, 100, 100, 100, 100, 150, 200, 100};
      this.columnAlignment = new int[]{16384, 16384, 16384, 131072, 131072, 131072, 131072, 131072, 16384, 16384, 16384, 16384};
      this.gSorter = new UploadersTableSorter(this);
      this.tableContentProvider = new UploadersTableContentProvider(this);
      this.tableLabelProvider = new UploadersTableLabelProvider(this);
      this.tableMenuListener = new UploadersTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((UploadersTableMenuListener)this.tableMenuListener);
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         if (PreferenceLoader.loadBoolean("pollUploaders")) {
            this.sViewer.setInput(this.getCore().getClientCollection().getUploadersWeakMap());
         } else {
            this.sViewer.setInput(null);
         }
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (PreferenceLoader.loadBoolean("pollUploaders")) {
         if (this.sViewer.getInput() == null) {
            this.setInput();
         }
      } else if (this.sViewer.getInput() != null) {
         this.sViewer.setInput(null);
      }
   }
}
