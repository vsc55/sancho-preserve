package sancho.view.transfer.fileDialog;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class SubfilesTableView extends GTableView {
   public static final int NAME = 0;
   public static final int SIZE = 1;
   public static final int MAGIC = 2;
   IPreview iPreview;
   List itemList = new ArrayList();

   public SubfilesTableView(ViewFrame viewFrame, IPreview preview) {
      super(viewFrame);
      this.iPreview = preview;
      this.fillList(preview);
      this.preferenceString = "fd.subFiles";
      this.columnLabels = new String[]{"fd.subFiles.name", "fd.subFiles.size", "fd.subFiles.magic"};
      this.columnAlignment = new int[]{16384, 131072, 16384};
      this.columnDefaultWidths = new int[]{250, 75, 250};
      this.gSorter = new SubfilesTableSorter(this);
      this.tableContentProvider = new SubfilesTableContentProvider(this);
      this.tableLabelProvider = new SubfilesTableLabelProvider(this);
      this.tableMenuListener = new SubfilesTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   public IPreview getIPreview() {
      return this.iPreview;
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((SubfilesTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
      this.updateHeader();
   }

   public void setInput() {
      this.sViewer.setInput(this.itemList);
   }

   public void updateHeader() {
      this.getViewFrame().updateCLabelText(SResources.getString("l.subFiles") + ": " + this.getTable().getItemCount());
   }

   protected void fillList(IPreview preview) {
      String[] names = preview.getSubFileNames();
      long[] sizes = preview.getSubFileSizes();
      String[] magics = preview.getSubFileMagics();
      if (names != null) {
         for (int i = 0; i < names.length; i++) {
            String magic = "";
            if (magics != null && magics.length > i) {
               magic = magics[i];
            }

            this.itemList.add(new SubfileItem(i, names[i], sizes[i], magic));
         }
      }
   }
}
