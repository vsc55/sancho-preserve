package sancho.view.viewer.table;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.IDSelector;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.GView;
import sancho.view.viewer.IGContentProvider;

public abstract class GTableView extends GView {
   protected GTableContentProvider tableContentProvider;
   protected GTableMenuListener tableMenuListener;

   public GTableView(ViewFrame var1) {
      this.viewFrame = var1;
   }

   public void setInput(Object var1) {
      this.input = var1;
   }

   public void setRedraw(boolean var1) {
   }

   protected void createContents(Composite var1) {
      this.sViewer = new CustomTableViewer(var1, 268500994);
      super.createContents();
   }

   public Table getTable() {
      return ((CustomTableViewer)this.sViewer).getTable();
   }

   protected void disposeAllColumns() {
      TableColumn[] var1 = this.getTable().getColumns();

      for (int var2 = var1.length - 1; var2 > -1; var2--) {
         var1[var2].dispose();
      }
   }

   public Item getItemAt(int var1, int var2) {
      return this.getTable().getItem(new Point(var1, var2));
   }

   public int getColumnWidthsExcept(int var1) {
      int var2 = 0;
      TableColumn[] var3 = this.getTable().getColumns();

      for (int var4 = 0; var4 < var3.length; var4++) {
         if (var4 != var1) {
            var2 += var3[var4].getWidth();
         }
      }

      return var2;
   }

   public String getColumnText(int var1) {
      return this.getTable().getColumn(var1).getText();
   }

   public void setColumnWidth(int var1, int var2) {
      this.getTable().getColumn(var1).setWidth(var2);
   }

   protected void onMove() {
      int[] var1 = this.getTable().getColumnOrder();
      if (this.columnIDs != null && var1 != null) {
         String var2 = "";

         for (int var3 = 0; var3 < var1.length; var3++) {
            var2 = var2 + this.columnIDs.charAt(var1[var3]);
         }

         if (this.columnIDs.length() == var2.length()) {
            PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "TableColumns", var2);
         }
      }
   }

   protected void createColumns() {
      this.columnIDs = IDSelector.loadIDs(this.preferenceString + "TableColumns", this.allColumns);
      ((ICustomViewer)this.getViewer()).setColumnIDs(this.columnIDs);
      PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
      Table var2 = this.getTable();
      var2.setHeaderVisible(true);
      TableColumn[] var3 = var2.getColumns();

      for (int var4 = var3.length - 1; var4 > -1; var4--) {
         var3[var4].dispose();
      }

      for (int var5 = 0; var5 < this.columnIDs.length(); var5++) {
         int var7 = this.columnIDs.charAt(var5) - 'A';
         TableColumn var8 = new TableColumn(var2, this.columnAlignment[var7]);
         var8.setMoveable(true);
         var1.setDefault(this.columnLabels[var7], this.columnDefaultWidths[var7]);
         var8.setText(SResources.getString(this.columnLabels[var7]));
         var8.setToolTipText(SResources.getString(this.columnLabels[var7] + ".tooltip"));
         int var9 = var1.getInt(this.columnLabels[var7]);
         var8.setWidth(var9 > 0 ? var9 : this.columnDefaultWidths[var7]);
         var8.addListener(10, new GTableView$1(this));
         var8.addDisposeListener(new GTableView$2(this, var1, var7));
         if (this.preferenceString.equals("result")) {
            var8.addControlListener(new GTableView$3(this, var1, var7));
         }

         var8.addListener(13, new GTableView$4(this, var5));
      }
   }

   public void setSortIndicator() {
      if (PreferenceLoader.loadBoolean("tableSortIndicator")) {
         int var1 = this.getSortColumn();
         TableColumn[] var2 = this.getTable().getColumns();
         if (var1 < var2.length) {
            int var3 = this.gSorter.getDirection() ? 1024 : 128;
            Table var4 = this.getTable();
            var4.setSortColumn(var2[var1]);
            var4.setSortDirection(var3);
         }
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (!PreferenceLoader.loadBoolean("tableSortIndicator")) {
         this.getTable().setSortColumn(null);
      }

      this.getTable().setBackground(PreferenceLoader.loadColor("tablesBackgroundColor"));
   }

   public void deselectAll() {
      this.getTable().deselectAll();
   }

   public void selectAll() {
      this.getTable().selectAll();
   }

   public int getItemCount() {
      return this.getTable().getItemCount();
   }

   public int[] getColumnOrder() {
      return ((CustomTableViewer)this.sViewer).getTable().getColumnOrder();
   }

   public void setLinesVisible(boolean var1) {
      this.getTable().setLinesVisible(var1);
   }

   public int getColumnCount() {
      return this.getTable().getColumnCount();
   }

   public void setFont(Font var1) {
      this.getTable().setFont(var1);
   }

   public Composite getComposite() {
      return ((CustomTableViewer)this.sViewer).getTable();
   }

   public IGContentProvider getContentProvider() {
      return this.tableContentProvider;
   }

   public GTableContentProvider getTableContentProvider() {
      return this.tableContentProvider;
   }

   public GTableMenuListener getTableMenuListener() {
      return this.tableMenuListener;
   }

   protected CustomTableViewer getTableViewer() {
      return (CustomTableViewer)this.sViewer;
   }

   // $VF: synthetic method
   static String[] access$000(GTableView var0) {
      return var0.columnLabels;
   }

   // $VF: synthetic method
   static String[] access$100(GTableView var0) {
      return var0.columnLabels;
   }
}
