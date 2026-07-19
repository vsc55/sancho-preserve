package sancho.view.viewer.table;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.CustomTableViewer;
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

   public GTableView(ViewFrame viewFrame) {
      this.viewFrame = viewFrame;
   }

   public void setInput(Object input) {
      this.input = input;
   }

   public void setRedraw(boolean redraw) {
   }

   protected void createContents(Composite composite) {
      this.sViewer = new CustomTableViewer(composite, 268500994);
      super.createContents();
   }

   public Table getTable() {
      return ((CustomTableViewer)this.sViewer).getTable();
   }

   protected void disposeAllColumns() {
      TableColumn[] columns = this.getTable().getColumns();

      for (int i = columns.length - 1; i > -1; i--) {
         columns[i].dispose();
      }
   }

   public Item getItemAt(int x, int y) {
      return this.getTable().getItem(new Point(x, y));
   }

   public int getColumnWidthsExcept(int exceptIndex) {
      int totalWidth = 0;
      TableColumn[] columns = this.getTable().getColumns();

      for (int i = 0; i < columns.length; i++) {
         if (i != exceptIndex) {
            totalWidth += columns[i].getWidth();
         }
      }

      return totalWidth;
   }

   public String getColumnText(int index) {
      return this.getTable().getColumn(index).getText();
   }

   public void setColumnWidth(int index, int width) {
      this.getTable().getColumn(index).setWidth(width);
   }

   protected void onMove() {
      int[] columnOrder = this.getTable().getColumnOrder();
      if (this.columnIDs != null && columnOrder != null) {
         String columnString = "";

         for (int i = 0; i < columnOrder.length; i++) {
            columnString = columnString + this.columnIDs.charAt(columnOrder[i]);
         }

         if (this.columnIDs.length() == columnString.length()) {
            PreferenceLoader.getPreferenceStore().setValue(this.preferenceString + "TableColumns", columnString);
         }
      }
   }

   protected void createColumns() {
      this.columnIDs = IDSelector.loadIDs(this.preferenceString + "TableColumns", this.allColumns);
      ((ICustomViewer)this.getViewer()).setColumnIDs(this.columnIDs);
      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      Table table = this.getTable();
      table.setHeaderVisible(true);
      TableColumn[] columns = table.getColumns();

      for (int i = columns.length - 1; i > -1; i--) {
         columns[i].dispose();
      }

      for (int i = 0; i < this.columnIDs.length(); i++) {
         int columnDefIndex = this.columnIDs.charAt(i) - 'A';
         TableColumn column = new TableColumn(table, this.columnAlignment[columnDefIndex]);
         column.setMoveable(true);
         preferenceStore.setDefault(this.columnLabels[columnDefIndex], this.columnDefaultWidths[columnDefIndex]);
         column.setText(SResources.getString(this.columnLabels[columnDefIndex]));
         column.setToolTipText(SResources.getString(this.columnLabels[columnDefIndex] + ".tooltip"));
         int width = preferenceStore.getInt(this.columnLabels[columnDefIndex]);
         column.setWidth(width > 0 ? width : this.columnDefaultWidths[columnDefIndex]);
         column.addListener(10, new Listener() {
            public void handleEvent(Event event) {
               onMove();
            }
         });
         column.addDisposeListener(new DisposeListener() {
            public synchronized void widgetDisposed(DisposeEvent disposeEvent) {
               TableColumn tableColumn = (TableColumn)disposeEvent.widget;
               if (tableColumn.getWidth() > 0) {
                  preferenceStore.setValue(columnLabels[columnDefIndex], tableColumn.getWidth());
               }
            }
         });
         if (this.preferenceString.equals("result")) {
            column.addControlListener(new ControlAdapter() {
               public void controlResized(ControlEvent controlEvent) {
                  TableColumn tableColumn = (TableColumn)controlEvent.widget;
                  if (tableColumn.getWidth() > 0) {
                     preferenceStore.setValue(columnLabels[columnDefIndex], tableColumn.getWidth());
                  }
               }
            });
         }

         final int columnIndex = i;
         column.addListener(13, new Listener() {
            public void handleEvent(Event event) {
               sortByColumn(columnIndex);
               setSortIndicator();
            }
         });
      }
   }

   public void setSortIndicator() {
      if (PreferenceLoader.loadBoolean("tableSortIndicator")) {
         int sortColumn = this.getSortColumn();
         TableColumn[] columns = this.getTable().getColumns();
         if (sortColumn < columns.length) {
            int direction = this.gSorter.getDirection() ? 1024 : 128;
            Table table = this.getTable();
            table.setSortColumn(columns[sortColumn]);
            table.setSortDirection(direction);
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

   public void setLinesVisible(boolean visible) {
      this.getTable().setLinesVisible(visible);
   }

   public int getColumnCount() {
      return this.getTable().getColumnCount();
   }

   public void setFont(Font font) {
      this.getTable().setFont(font);
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
}
