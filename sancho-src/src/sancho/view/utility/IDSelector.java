package sancho.view.utility;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;

public class IDSelector extends Dialog {
   public static final int MAGIC_NUMBER = 65;
   private String[] legend;
   private String allIDs;
   private String leftIDs;
   private String rightIDs;
   private String prefOption;
   private String prefOptionOff;
   private TableItem hoverTableItem;
   private Table table;
   private boolean dragging;
   private String propString;

   public IDSelector(Shell parentShell, String[] legend, String prefPrefix, String prefSuffix, String propName) {
      super(parentShell);
      this.legend = legend;
      this.allIDs = "";

      for (int i = 0; i < legend.length; i++) {
         this.allIDs = this.allIDs + String.valueOf((char)(65 + i));
      }

      this.propString = propName;
      this.prefOption = prefPrefix + prefSuffix;
      this.prefOptionOff = this.prefOption + "Off";
      String prefValue = PreferenceLoader.loadString(this.prefOption);
      this.leftIDs = PreferenceLoader.loadString(this.prefOptionOff);
      this.rightIDs = !prefValue.equals("") ? prefValue : this.allIDs;

      for (int i = 0; i < this.allIDs.length(); i++) {
         if (this.leftIDs.indexOf(this.allIDs.charAt(i)) == -1 && this.rightIDs.indexOf(this.allIDs.charAt(i)) == -1) {
            this.rightIDs = this.rightIDs + this.allIDs.charAt(i);
         }
      }
   }

   public Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      Label label = new Label(composite, 0);
      label.setText(SResources.getString("l.selectorInfo"));
      label.setLayoutData(gridData);
      this.createTable(composite);
      this.createButtons(composite);
      this.createDefault(composite);
      return composite;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString(this.propString) + " " + SResources.getString("l.selector"));
      shell.setImage(SResources.getImage("preferences"));
   }

   // Resizable/maximizable: the column table (GridData FILL_BOTH) grabs the extra
   // space, so a taller/wider window shows more columns at once.
   protected int getShellStyle() {
      return super.getShellStyle() | SWT.RESIZE | SWT.MAX;
   }

   protected void createDefault(Composite parent) {
      GridData gridData = new GridData(768);
      gridData.horizontalSpan = 2;
      Button button = new Button(parent, 0);
      button.setText(SResources.getString("l.default"));
      button.setLayoutData(gridData);
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent selectionEvent) {
            IDSelector.this.rightIDs = IDSelector.this.allIDs;
            IDSelector.this.leftIDs = "";
            IDSelector.this.table.removeAll();
            IDSelector.this.createItems();
         }
      });
   }

   public void createTable(Composite parent) {
      this.table = new Table(parent, 67620);
      this.table.setLayoutData(new GridData(1808));
      Listener listener = new Listener() {
         public void handleEvent(Event event) {
            switch (event.type) {
               case 3:
                  IDSelector.this.onMouseDown(event);
                  break;
               case 4:
                  IDSelector.this.onMouseUp(event);
                  break;
               case 5:
                  IDSelector.this.onMouseMove(event);
            }
         }
      };
      int[] eventTypes = new int[]{3, 4, 5};

      for (int i = 0; i < eventTypes.length; i++) {
         this.table.addListener(eventTypes[i], listener);
      }

      this.createItems();
   }

   public void onMouseMove(Event event) {
      Table table = (Table)event.widget;
      TableItem tableItem = table.getItem(new Point(event.x, event.y));
      if (tableItem != null) {
         if (this.hoverTableItem != null && this.hoverTableItem != tableItem) {
            this.hoverTableItem.setBackground(null);
         }

         if (this.dragging) {
            this.hoverTableItem = tableItem;
            this.hoverTableItem.setBackground(table.getDisplay().getSystemColor(22));
         }
      } else {
         this.dragging = false;
         if (this.hoverTableItem != null && !this.hoverTableItem.isDisposed()) {
            this.hoverTableItem.setBackground(null);
            this.hoverTableItem = null;
         }
      }
   }

   public void onMouseUp(Event event) {
      Table table = (Table)event.widget;
      TableItem tableItem = table.getItem(new Point(event.x, event.y));
      if (this.dragging) {
         if (this.hoverTableItem != null && table.getSelection().length > 0) {
            TableItem selectedItem = table.getSelection()[0];
            if (selectedItem != tableItem) {
               int selectionIndex = table.getSelectionIndex();
               boolean checked = selectedItem.getChecked();
               String id = (String)selectedItem.getData("ID");
               int legendIndex = id.charAt(0) - 'A';
               table.remove(selectionIndex);
               int newIndex = table.indexOf(this.hoverTableItem) + 1;
               TableItem newItem = new TableItem(table, 0, newIndex);
               newItem.setData("ID", id);
               newItem.setText(SResources.getString(this.legend[legendIndex]));
               newItem.setChecked(checked);
               table.setSelection(newIndex);
               this.hoverTableItem.setBackground(null);
               this.hoverTableItem = null;
            }
         }

         this.dragging = false;
      }
   }

   public void onMouseDown(Event event) {
      Table table = (Table)event.widget;
      TableItem tableItem = table.getItem(new Point(event.x, event.y));
      if (tableItem != null) {
         this.dragging = true;
         this.hoverTableItem = null;
      }
   }

   public void createItems() {
      for (int i = 0; i < this.rightIDs.length(); i++) {
         TableItem tableItem = new TableItem(this.table, 0);
         int legendIndex = this.rightIDs.charAt(i) - 'A';
         tableItem.setData("ID", "" + this.rightIDs.charAt(i));
         tableItem.setText(SResources.getString(this.legend[legendIndex]));
         tableItem.setChecked(true);
      }

      for (int i = 0; i < this.leftIDs.length(); i++) {
         TableItem tableItem = new TableItem(this.table, 0);
         int legendIndex = this.leftIDs.charAt(i) - 'A';
         tableItem.setData("ID", "" + this.leftIDs.charAt(i));
         tableItem.setText(SResources.getString(this.legend[legendIndex]));
      }
   }

   public void createButtons(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 5, 5, false));
      composite.setLayoutData(new GridData(1808));
      Button upButton = new Button(composite, 0);
      upButton.setText(SResources.getString("l.up"));
      upButton.setLayoutData(new GridData(1808));
      upButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent selectionEvent) {
            int selectionIndex;
            if ((selectionIndex = IDSelector.this.table.getSelectionIndex()) > 0) {
               IDSelector.this.moveItem(selectionIndex, -1);
            }
         }
      });
      Button downButton = new Button(composite, 0);
      downButton.setText(SResources.getString("l.down"));
      downButton.setLayoutData(new GridData(1808));
      downButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent selectionEvent) {
            int selectionIndex = IDSelector.this.table.getSelectionIndex();
            if (selectionIndex < IDSelector.this.table.getItemCount() - 1 && selectionIndex > -1) {
               IDSelector.this.moveItem(selectionIndex, 1);
            }
         }
      });
   }

   public void moveItem(int index, int offset) {
      TableItem tableItem = this.table.getItem(index);
      boolean checked = tableItem.getChecked();
      String id = (String)tableItem.getData("ID");
      int legendIndex = id.charAt(0) - 'A';
      this.table.remove(index);
      TableItem newItem = new TableItem(this.table, 0, index + offset);
      newItem.setData("ID", id);
      newItem.setText(SResources.getString(this.legend[legendIndex]));
      newItem.setChecked(checked);
      this.table.setSelection(index + offset);
   }

   public void savePrefs() {
      if (this.rightIDs.length() > 1) {
         PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
         preferenceStore.setValue(this.prefOption, this.rightIDs);
         preferenceStore.setValue(this.prefOptionOff, this.leftIDs);
         PreferenceLoader.saveStore();
      }

      if (Sancho.getCore() != null) {
         Sancho.getCore().updatePreferences();
      }
   }

   public void refreshLists() {
      TableItem[] items = this.table.getItems();
      this.leftIDs = "";
      this.rightIDs = "";

      for (int i = 0; i < items.length; i++) {
         if (items[i].getChecked()) {
            this.rightIDs = this.rightIDs + (String)items[i].getData("ID");
         } else {
            this.leftIDs = this.leftIDs + (String)items[i].getData("ID");
         }
      }
   }

   protected void buttonPressed(int buttonId) {
      this.refreshLists();
      super.buttonPressed(buttonId);
   }

   public static String createIDString(String[] legend) {
      StringBuffer buffer = new StringBuffer();

      for (int i = 0; i < legend.length; i++) {
         buffer.append(String.valueOf((char)(65 + i)));
      }

      return buffer.toString().intern();
   }

   public static String getID(int index) {
      return String.valueOf((char)(65 + index));
   }

   public static String loadIDs(String prefName, String allIDs) {
      String prefNameOff = prefName + "Off";
      String rightIDs = PreferenceLoader.loadString(prefName);
      String leftIDs = PreferenceLoader.loadString(prefNameOff);

      for (int i = 0; i < rightIDs.length(); i++) {
         if (allIDs.indexOf(rightIDs.charAt(i)) == -1) {
            PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
            preferenceStore.setValue(prefName, allIDs);
            preferenceStore.setValue(prefNameOff, "");
            return allIDs;
         }
      }

      for (int i = 0; i < leftIDs.length(); i++) {
         if (allIDs.indexOf(leftIDs.charAt(i)) == -1) {
            PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
            leftIDs = "";
            preferenceStore.setValue(prefNameOff, "");
         }
      }

      for (int i = 0; i < allIDs.length(); i++) {
         if (rightIDs.indexOf(allIDs.charAt(i)) == -1 && leftIDs.indexOf(allIDs.charAt(i)) == -1) {
            rightIDs = rightIDs + allIDs.charAt(i);
         }
      }

      String filteredIDs = "";

      for (int i = 0; i < rightIDs.length(); i++) {
         if (leftIDs.indexOf(rightIDs.charAt(i)) == -1) {
            filteredIDs = filteredIDs + rightIDs.charAt(i);
         }
      }

      PreferenceStore preferenceStore = PreferenceLoader.getPreferenceStore();
      preferenceStore.setValue(prefName, filteredIDs);
      preferenceStore.setValue(prefNameOff, leftIDs);
      return filteredIDs.intern();
   }
}
