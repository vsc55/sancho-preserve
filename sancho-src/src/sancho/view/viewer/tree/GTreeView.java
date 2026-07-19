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

   public GTreeView(ViewFrame viewFrame) {
      this.viewFrame = viewFrame;
   }

   protected void createContents(Composite composite) {
      this.sViewer = new CustomTreeViewer(composite, 268500994);
      super.createContents();
   }

   public Composite getComposite() {
      return this.getTree();
   }

   public Item getItemAt(int x, int y) {
      return this.getTree().getItem(new Point(x, y));
   }

   public int getItemCount() {
      return this.getTree().getItemCount();
   }

   protected Tree getTree() {
      return ((CustomTreeViewer)this.sViewer).getTree();
   }

   public void setLinesVisible(boolean visible) {
      this.getTree().setLinesVisible(visible);
   }

   public void setFont(Font font) {
      this.getTree().setFont(font);
   }

   public int getColumnCount() {
      return this.getTree().getColumnCount();
   }

   protected void disposeAllColumns() {
      TreeColumn[] columns = this.getTree().getColumns();

      for (int i = columns.length - 1; i > -1; i--) {
         columns[i].dispose();
      }
   }

   public int getColumnWidthsExcept(int columnIndex) {
      int totalWidth = 0;
      TreeColumn[] columns = this.getTree().getColumns();

      for (int i = 0; i < columns.length; i++) {
         if (i != columnIndex) {
            totalWidth += columns[i].getWidth();
         }
      }

      return totalWidth;
   }

   public String getColumnText(int columnIndex) {
      return this.getTree().getColumn(columnIndex).getText();
   }

   public void setColumnWidth(int columnIndex, int width) {
      this.getTree().getColumn(columnIndex).setWidth(width);
   }

   protected void onMove() {
      int[] columnOrder = this.getTree().getColumnOrder();
      if (this.columnIDs != null && columnOrder != null) {
         String orderedIDs = "";

         for (int i = 0; i < columnOrder.length; i++) {
            orderedIDs = orderedIDs + this.columnIDs.charAt(columnOrder[i]);
         }

         if (this.columnIDs.length() == orderedIDs.length()) {
            PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "TableColumns", orderedIDs);
         }
      }
   }

   protected void createColumns() {
      this.columnIDs = IDSelector.loadIDs(this.preferenceString + "TableColumns", this.allColumns);
      ((ICustomViewer)this.getViewer()).setColumnIDs(this.columnIDs);
      PreferenceStore prefStore = PreferenceLoader.getPreferenceStore();
      Tree tree = this.getTree();
      tree.setHeaderVisible(true);
      TreeColumn[] columns = tree.getColumns();

      for (int i = columns.length - 1; i > -1; i--) {
         columns[i].dispose();
      }

      for (int i = 0; i < this.columnIDs.length(); i++) {
         int columnID = this.columnIDs.charAt(i) - 'A';
         final PreferenceStore preferenceStore = prefStore;
         final int arrayItem = columnID;
         final int columnIndex = i;
         TreeColumn column = new TreeColumn(tree, this.columnAlignment[columnID]);
         column.setMoveable(true);
         prefStore.setDefault(this.columnLabels[columnID], this.columnDefaultWidths[columnID]);
         column.setText(SResources.getString(this.columnLabels[columnID]));
         column.setToolTipText(SResources.getString(this.columnLabels[columnID] + ".tooltip"));
         int width = prefStore.getInt(this.columnLabels[columnID]);
         column.setWidth(width > 0 ? width : this.columnDefaultWidths[columnID]);
         column.addListener(10, new Listener() {
            public void handleEvent(Event event) {
               GTreeView.this.onMove();
            }
         });
         column.addDisposeListener(new DisposeListener() {
            public synchronized void widgetDisposed(DisposeEvent event) {
               TreeColumn column = (TreeColumn)event.widget;
               if (column.getWidth() > 0) {
                  preferenceStore.setValue(GTreeView.this.columnLabels[arrayItem], column.getWidth());
               }
            }
         });
         if (this.preferenceString.equals("result")) {
            column.addControlListener(new ControlAdapter() {
               public void controlResized(ControlEvent event) {
                  TableColumn column = (TableColumn)event.widget;
                  if (column.getWidth() > 0) {
                     preferenceStore.setValue(GTreeView.this.columnLabels[arrayItem], column.getWidth());
                  }
               }
            });
         }

         column.addListener(13, new Listener() {
            public void handleEvent(Event event) {
               GTreeView.this.sortByColumn(columnIndex);
               GTreeView.this.setSortIndicator();
            }
         });
      }
   }

   public void setSortIndicator() {
      if (PreferenceLoader.loadBoolean("tableSortIndicator")) {
         int sortColumn = this.getSortColumn();
         TreeColumn[] columns = this.getTree().getColumns();
         if (sortColumn < columns.length) {
            int direction = this.gSorter.getDirection() ? 1024 : 128;
            Tree tree = this.getTree();
            tree.setSortColumn(columns[sortColumn]);
            tree.setSortDirection(direction);
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
