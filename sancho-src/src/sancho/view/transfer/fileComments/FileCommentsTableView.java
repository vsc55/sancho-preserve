package sancho.view.transfer.fileComments;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class FileCommentsTableView extends GTableView {
   public static final int NAME = 0;
   public static final int ADDR = 1;
   public static final int RATING = 2;
   public static final int COMMENT = 3;
   File file;

   public FileCommentsTableView(ViewFrame var1, File var2) {
      super(var1);
      this.file = var2;
      this.preferenceString = "fd.fileComments";
      this.columnLabels = new String[]{"fd.fileComments.name", "fd.fileComments.addr", "fd.fileComments.rating", "fd.fileComments.comment"};
      this.columnAlignment = new int[]{16384, 131072, 131072, 16384};
      this.columnDefaultWidths = new int[]{250, 75, 75, 250};
      this.gSorter = new FileCommentsTableSorter(this);
      this.tableContentProvider = new FileCommentsTableContentProvider(this);
      this.tableLabelProvider = new FileCommentsTableLabelProvider(this);
      this.tableMenuListener = new FileCommentsTableMenuListener(this);
      this.createContents(var1.getChildComposite());
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
      this.sViewer.addSelectionChangedListener((FileCommentsTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
      this.updateHeader();
   }

   public void setInput() {
      this.sViewer.setInput(this.file);
   }

   public void refresh() {
      super.refresh();
      this.updateHeader();
   }

   public void updateHeader() {
      StringBuffer var1 = new StringBuffer(64);
      var1.append(SResources.getString("l.fileComments"));
      var1.append(": ");
      var1.append(this.getTable().getItemCount());
      var1.append(" / ");
      var1.append(SResources.getString("l.avgRating"));
      var1.append(": ");
      var1.append(this.file.getAvgRating());
      this.getViewFrame().updateCLabelText(var1.toString());
   }
}
