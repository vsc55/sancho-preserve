package sancho.view.preferences;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

class WinRegPreferencePage$RegisterLink {
   public static final int NO_CHANGE = 0;
   public static final int REGISTER = 1;
   public static final int UNREGISTER = 2;
   private int selection;
   private String text;

   public WinRegPreferencePage$RegisterLink(String var1, Composite var2) {
      this.text = var1;
      this.selection = 0;
      this.createContents(var2);
   }

   protected void createContents(Composite var1) {
      Group var2 = new Group(var1, 16);
      var2.setLayoutData(new GridData(768));
      var2.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
      var2.setText(this.text + "://");
      this.createButton(var2, SResources.getString("b.noChange"), 0);
      this.createButton(var2, SResources.getString("b.registerLink"), 1);
      this.createButton(var2, SResources.getString("b.unregisterLink"), 2);
   }

   private void createButton(Group var1, String var2, int var3) {
      Button var4 = new Button(var1, 16);
      var4.setLayoutData(new GridData(768));
      var4.setText(var2);
      var4.setSelection(var3 == 0);
      var4.addSelectionListener(new WinRegPreferencePage$3(this, var3));
   }

   public int getSelection() {
      return this.selection;
   }

   public String getText() {
      return this.text;
   }

   // $VF: synthetic method
   static int access$302(WinRegPreferencePage$RegisterLink var0, int var1) {
      return var0.selection = var1;
   }
}
