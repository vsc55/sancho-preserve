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

   public FileCommentsTableView(ViewFrame viewFrame, File file) {
      super(viewFrame);
      this.file = file;
      this.preferenceString = "fd.fileComments";
      this.columnLabels = new String[]{"fd.fileComments.name", "fd.fileComments.addr", "fd.fileComments.rating", "fd.fileComments.comment"};
      this.columnAlignment = new int[]{16384, 131072, 131072, 16384};
      this.columnDefaultWidths = new int[]{250, 75, 75, 250};
      this.gSorter = new FileCommentsTableSorter(this);
      this.tableContentProvider = new FileCommentsTableContentProvider(this);
      this.tableLabelProvider = new FileCommentsTableLabelProvider(this);
      this.tableMenuListener = new FileCommentsTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
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
      StringBuffer buffer = new StringBuffer(64);
      buffer.append(SResources.getString("l.fileComments"));
      buffer.append(": ");
      buffer.append(this.getTable().getItemCount());
      buffer.append(" / ");
      buffer.append(SResources.getString("l.avgRating"));
      buffer.append(": ");
      buffer.append(this.file.getAvgRating());
      this.getViewFrame().updateCLabelText(buffer.toString());
   }
}
