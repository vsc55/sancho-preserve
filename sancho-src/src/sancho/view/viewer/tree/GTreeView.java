package sancho.view.viewer.tree;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.jface.viewers.ICustomViewer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.IDSelector;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.GView;
import sancho.view.viewer.IGContentProvider;
import sancho.view.viewer.table.GTableMenuListener;

public abstract class GTreeView extends GView {
   protected GTreeContentProvider treeContentProvider;
   protected GTableMenuListener tableMenuListener;

   public GTreeView(ViewFrame var1) {
      this.viewFrame = var1;
   }

   protected void createContents(Composite var1) {
      this.sViewer = new CustomTreeViewer(var1, 268500994);
      super.createContents();
   }

   public Composite getComposite() {
      return this.getTree();
   }

   public Item getItemAt(int var1, int var2) {
      return this.getTree().getItem(new Point(var1, var2));
   }

   public int getItemCount() {
      return this.getTree().getItemCount();
   }

   protected Tree getTree() {
      return ((CustomTreeViewer)this.sViewer).getTree();
   }

   public void setLinesVisible(boolean var1) {
      this.getTree().setLinesVisible(var1);
   }

   public void setFont(Font var1) {
      this.getTree().setFont(var1);
   }

   public int getColumnCount() {
      return this.getTree().getColumnCount();
   }

   protected void disposeAllColumns() {
      TreeColumn[] var1 = this.getTree().getColumns();

      for (int var2 = var1.length - 1; var2 > -1; var2--) {
         var1[var2].dispose();
      }
   }

   public int getColumnWidthsExcept(int var1) {
      int var2 = 0;
      TreeColumn[] var3 = this.getTree().getColumns();

      for (int var4 = 0; var4 < var3.length; var4++) {
         if (var4 != var1) {
            var2 += var3[var4].getWidth();
         }
      }

      return var2;
   }

   public String getColumnText(int var1) {
      return this.getTree().getColumn(var1).getText();
   }

   public void setColumnWidth(int var1, int var2) {
      this.getTree().getColumn(var1).setWidth(var2);
   }

   protected void onMove() {
      int[] var1 = this.getTree().getColumnOrder();
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
      Tree var2 = this.getTree();
      var2.setHeaderVisible(true);
      TreeColumn[] var3 = var2.getColumns();

      for (int var4 = var3.length - 1; var4 > -1; var4--) {
         var3[var4].dispose();
      }

      for (int var5 = 0; var5 < this.columnIDs.length(); var5++) {
         int var7 = this.columnIDs.charAt(var5) - 'A';
         final PreferenceStore preferenceStore = var1;
         final int arrayItem = var7;
         final int columnIndex = var5;
         TreeColumn var8 = new TreeColumn(var2, this.columnAlignment[var7]);
         var8.setMoveable(true);
         var1.setDefault(this.columnLabels[var7], this.columnDefaultWidths[var7]);
         var8.setText(SResources.getString(this.columnLabels[var7]));
         var8.setToolTipText(SResources.getString(this.columnLabels[var7] + ".tooltip"));
         int var9 = var1.getInt(this.columnLabels[var7]);
         var8.setWidth(var9 > 0 ? var9 : this.columnDefaultWidths[var7]);
         var8.addListener(10, new Listener() {
            public void handleEvent(Event var1) {
               GTreeView.this.onMove();
            }
         });
         var8.addDisposeListener(new DisposeListener() {
            public synchronized void widgetDisposed(DisposeEvent var1) {
               TreeColumn var2 = (TreeColumn)var1.widget;
               if (var2.getWidth() > 0) {
                  preferenceStore.setValue(GTreeView.this.columnLabels[arrayItem], var2.getWidth());
               }
            }
         });
         if (this.preferenceString.equals("result")) {
            var8.addControlListener(new ControlAdapter() {
               public void controlResized(ControlEvent var1) {
                  TableColumn var2 = (TableColumn)var1.widget;
                  if (var2.getWidth() > 0) {
                     preferenceStore.setValue(GTreeView.this.columnLabels[arrayItem], var2.getWidth());
                  }
               }
            });
         }

         var8.addListener(13, new Listener() {
            public void handleEvent(Event var1) {
               GTreeView.this.sortByColumn(columnIndex);
               GTreeView.this.setSortIndicator();
            }
         });
      }
   }

   public void setSortIndicator() {
      if (PreferenceLoader.loadBoolean("tableSortIndicator")) {
         int var1 = this.getSortColumn();
         TreeColumn[] var2 = this.getTree().getColumns();
         if (var1 < var2.length) {
            int var3 = this.gSorter.getDirection() ? 1024 : 128;
            Tree var4 = this.getTree();
            var4.setSortColumn(var2[var1]);
            var4.setSortDirection(var3);
         }
      }
   }

   public void updateDisplay() {
      super.updateDisplay();
      if (!PreferenceLoader.loadBoolean("tableSortIndicator")) {
         this.getTree().setSortColumn(null);
      }

      this.getTree().setBackground(PreferenceLoader.loadColor("tablesBackgroundColor"));
   }

   public void deselectAll() {
      this.getTree().deselectAll();
   }

   public void selectAll() {
      this.getTree().selectAll();
   }

   public GTreeContentProvider getTreeContentProvider() {
      return this.treeContentProvider;
   }

   public IGContentProvider getContentProvider() {
      return this.treeContentProvider;
   }

   public GTableMenuListener getTableMenuListener() {
      return this.tableMenuListener;
   }

   protected CustomTreeViewer getTreeViewer() {
      return (CustomTreeViewer)this.sViewer;
   }
}
