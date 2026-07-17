package sancho.view.utility;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
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

   public IDSelector(Shell var1, String[] var2, String var3, String var4, String var5) {
      super(var1);
      this.legend = var2;
      this.allIDs = "";

      for (int var6 = 0; var6 < var2.length; var6++) {
         this.allIDs = this.allIDs + String.valueOf((char)(65 + var6));
      }

      this.propString = var5;
      this.prefOption = var3 + var4;
      this.prefOptionOff = this.prefOption + "Off";
      String var7 = PreferenceLoader.loadString(this.prefOption);
      this.leftIDs = PreferenceLoader.loadString(this.prefOptionOff);
      this.rightIDs = !var7.equals("") ? var7 : this.allIDs;

      for (int var8 = 0; var8 < this.allIDs.length(); var8++) {
         if (this.leftIDs.indexOf(this.allIDs.charAt(var8)) == -1 && this.rightIDs.indexOf(this.allIDs.charAt(var8)) == -1) {
            this.rightIDs = this.rightIDs + this.allIDs.charAt(var8);
         }
      }
   }

   public Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      GridData var3 = new GridData(768);
      var3.horizontalSpan = 2;
      Label var4 = new Label(var2, 0);
      var4.setText(SResources.getString("l.selectorInfo"));
      var4.setLayoutData(var3);
      this.createTable(var2);
      this.createButtons(var2);
      this.createDefault(var2);
      return var2;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString(this.propString) + " " + SResources.getString("l.selector"));
      var1.setImage(SResources.getImage("preferences"));
   }

   protected void createDefault(Composite var1) {
      GridData var2 = new GridData(768);
      var2.horizontalSpan = 2;
      Button var3 = new Button(var1, 0);
      var3.setText(SResources.getString("l.default"));
      var3.setLayoutData(var2);
      var3.addSelectionListener(new IDSelector$1(this));
   }

   public void createTable(Composite var1) {
      this.table = new Table(var1, 67620);
      this.table.setLayoutData(new GridData(1808));
      IDSelector$2 var2 = new IDSelector$2(this);
      int[] var3 = new int[]{3, 4, 5};

      for (int var4 = 0; var4 < var3.length; var4++) {
         this.table.addListener(var3[var4], var2);
      }

      this.createItems();
   }

   public void onMouseMove(Event var1) {
      Table var2 = (Table)var1.widget;
      TableItem var3 = var2.getItem(new Point(var1.x, var1.y));
      if (var3 != null) {
         if (this.hoverTableItem != null && this.hoverTableItem != var3) {
            this.hoverTableItem.setBackground(null);
         }

         if (this.dragging) {
            this.hoverTableItem = var3;
            this.hoverTableItem.setBackground(var2.getDisplay().getSystemColor(22));
         }
      } else {
         this.dragging = false;
         if (this.hoverTableItem != null && !this.hoverTableItem.isDisposed()) {
            this.hoverTableItem.setBackground(null);
            this.hoverTableItem = null;
         }
      }
   }

   public void onMouseUp(Event var1) {
      Table var2 = (Table)var1.widget;
      TableItem var3 = var2.getItem(new Point(var1.x, var1.y));
      if (this.dragging) {
         if (this.hoverTableItem != null && var2.getSelection().length > 0) {
            TableItem var4 = var2.getSelection()[0];
            if (var4 != var3) {
               int var5 = var2.getSelectionIndex();
               boolean var6 = var4.getChecked();
               String var7 = (String)var4.getData("ID");
               int var8 = var7.charAt(0) - 'A';
               var2.remove(var5);
               int var9 = var2.indexOf(this.hoverTableItem) + 1;
               TableItem var10 = new TableItem(var2, 0, var9);
               var10.setData("ID", var7);
               var10.setText(SResources.getString(this.legend[var8]));
               var10.setChecked(var6);
               var2.setSelection(var9);
               this.hoverTableItem.setBackground(null);
               this.hoverTableItem = null;
            }
         }

         this.dragging = false;
      }
   }

   public void onMouseDown(Event var1) {
      Table var2 = (Table)var1.widget;
      TableItem var3 = var2.getItem(new Point(var1.x, var1.y));
      if (var3 != null) {
         this.dragging = true;
         this.hoverTableItem = null;
      }
   }

   public void createItems() {
      for (int var1 = 0; var1 < this.rightIDs.length(); var1++) {
         TableItem var2 = new TableItem(this.table, 0);
         int var3 = this.rightIDs.charAt(var1) - 'A';
         var2.setData("ID", "" + this.rightIDs.charAt(var1));
         var2.setText(SResources.getString(this.legend[var3]));
         var2.setChecked(true);
      }

      for (int var5 = 0; var5 < this.leftIDs.length(); var5++) {
         TableItem var6 = new TableItem(this.table, 0);
         int var4 = this.leftIDs.charAt(var5) - 'A';
         var6.setData("ID", "" + this.leftIDs.charAt(var5));
         var6.setText(SResources.getString(this.legend[var4]));
      }
   }

   public void createButtons(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(1, 0, 0, 5, 5, false));
      var2.setLayoutData(new GridData(1808));
      Button var3 = new Button(var2, 0);
      var3.setText(SResources.getString("l.up"));
      var3.setLayoutData(new GridData(1808));
      var3.addSelectionListener(new IDSelector$3(this));
      Button var4 = new Button(var2, 0);
      var4.setText(SResources.getString("l.down"));
      var4.setLayoutData(new GridData(1808));
      var4.addSelectionListener(new IDSelector$4(this));
   }

   public void moveItem(int var1, int var2) {
      TableItem var3 = this.table.getItem(var1);
      boolean var4 = var3.getChecked();
      String var5 = (String)var3.getData("ID");
      int var6 = var5.charAt(0) - 'A';
      this.table.remove(var1);
      TableItem var7 = new TableItem(this.table, 0, var1 + var2);
      var7.setData("ID", var5);
      var7.setText(SResources.getString(this.legend[var6]));
      var7.setChecked(var4);
      this.table.setSelection(var1 + var2);
   }

   public void savePrefs() {
      if (this.rightIDs.length() > 1) {
         PreferenceStore var1 = PreferenceLoader.getPreferenceStore();
         var1.setValue(this.prefOption, this.rightIDs);
         var1.setValue(this.prefOptionOff, this.leftIDs);
         PreferenceLoader.saveStore();
      }

      if (Sancho.getCore() != null) {
         Sancho.getCore().updatePreferences();
      }
   }

   public void refreshLists() {
      TableItem[] var1 = this.table.getItems();
      this.leftIDs = "";
      this.rightIDs = "";

      for (int var2 = 0; var2 < var1.length; var2++) {
         if (var1[var2].getChecked()) {
            this.rightIDs = this.rightIDs + (String)var1[var2].getData("ID");
         } else {
            this.leftIDs = this.leftIDs + (String)var1[var2].getData("ID");
         }
      }
   }

   protected void buttonPressed(int var1) {
      this.refreshLists();
      super.buttonPressed(var1);
   }

   public static String createIDString(String[] var0) {
      StringBuffer var1 = new StringBuffer();

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1.append(String.valueOf((char)(65 + var2)));
      }

      return var1.toString().intern();
   }

   public static String getID(int var0) {
      return String.valueOf((char)(65 + var0));
   }

   public static String loadIDs(String var0, String var1) {
      String var2 = var0 + "Off";
      String var3 = PreferenceLoader.loadString(var0);
      String var4 = PreferenceLoader.loadString(var2);

      for (int var5 = 0; var5 < var3.length(); var5++) {
         if (var1.indexOf(var3.charAt(var5)) == -1) {
            PreferenceStore var6 = PreferenceLoader.getPreferenceStore();
            var6.setValue(var0, var1);
            var6.setValue(var2, "");
            return var1;
         }
      }

      for (int var11 = 0; var11 < var4.length(); var11++) {
         if (var1.indexOf(var4.charAt(var11)) == -1) {
            PreferenceStore var7 = PreferenceLoader.getPreferenceStore();
            var4 = "";
            var7.setValue(var2, "");
         }
      }

      for (int var12 = 0; var12 < var1.length(); var12++) {
         if (var3.indexOf(var1.charAt(var12)) == -1 && var4.indexOf(var1.charAt(var12)) == -1) {
            var3 = var3 + var1.charAt(var12);
         }
      }

      String var8 = "";

      for (int var9 = 0; var9 < var3.length(); var9++) {
         if (var4.indexOf(var3.charAt(var9)) == -1) {
            var8 = var8 + var3.charAt(var9);
         }
      }

      PreferenceStore var10 = PreferenceLoader.getPreferenceStore();
      var10.setValue(var0, var8);
      var10.setValue(var2, var4);
      return var8.intern();
   }

   // $VF: synthetic method
   static String access$002(IDSelector var0, String var1) {
      return var0.rightIDs = var1;
   }

   // $VF: synthetic method
   static String access$100(IDSelector var0) {
      return var0.allIDs;
   }

   // $VF: synthetic method
   static String access$202(IDSelector var0, String var1) {
      return var0.leftIDs = var1;
   }

   // $VF: synthetic method
   static Table access$300(IDSelector var0) {
      return var0.table;
   }
}
