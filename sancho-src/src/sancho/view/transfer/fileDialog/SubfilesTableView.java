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

   public SubfilesTableView(ViewFrame var1, IPreview var2) {
      super(var1);
      this.iPreview = var2;
      this.fillList(var2);
      this.preferenceString = "fd.subFiles";
      this.columnLabels = new String[]{"fd.subFiles.name", "fd.subFiles.size", "fd.subFiles.magic"};
      this.columnAlignment = new int[]{16384, 131072, 16384};
      this.columnDefaultWidths = new int[]{250, 75, 250};
      this.gSorter = new SubfilesTableSorter(this);
      this.tableContentProvider = new SubfilesTableContentProvider(this);
      this.tableLabelProvider = new SubfilesTableLabelProvider(this);
      this.tableMenuListener = new SubfilesTableMenuListener(this);
      this.createContents(var1.getChildComposite());
   }

   public IPreview getIPreview() {
      return this.iPreview;
   }

   protected void createContents(Composite var1) {
      super.createContents(var1);
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

   protected void fillList(IPreview var1) {
      String[] var2 = var1.getSubFileNames();
      long[] var3 = var1.getSubFileSizes();
      String[] var4 = var1.getSubFileMagics();
      if (var2 != null) {
         for (int var5 = 0; var5 < var2.length; var5++) {
            String var6 = "";
            if (var4 != null && var4.length > var5) {
               var6 = var4[var5];
            }

            this.itemList.add(new SubfileItem(var5, var2[var5], var3[var5], var6));
         }
      }
   }
}
