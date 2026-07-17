package sancho.view.utility;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;

class VersionChecker$VersionDialog extends Dialog {
   String string;

   public VersionChecker$VersionDialog(Shell var1, String var2) {
      super(var1);
      this.string = var2;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setText(SResources.getString("l.newVersionTitle"));
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      Label var3 = new Label(var2, 0);
      GridData var4 = new GridData(832);
      var4.horizontalSpan = 2;
      var3.setLayoutData(var4);
      var3.setText(VersionInfo.getName() + ": " + SResources.getString("l.newVersionText"));
      this.createLabel(var2, SResources.getString("l.current"), 128);
      this.createLabel(var2, VersionInfo.getVersion(), 32);
      this.createLabel(var2, SResources.getString("l.latest"), 128);
      this.createLabel(var2, this.string, 32);
      return var2;
   }

   private void createLabel(Composite var1, String var2, int var3) {
      Label var4 = new Label(var1, 0);
      var4.setLayoutData(new GridData(512 | var3));
      var4.setText(var2);
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));
      var2.setLayoutData(new GridData(768));
      this.createButton(var2, 128, SResources.getString("b.close"), new VersionChecker$4(this));
      this.createButton(var2, 768, SResources.getString("l.visit") + " " + SResources.getString("ti.web.sancho"), new VersionChecker$5(this));
      return var2;
   }

   protected void createButton(Composite var1, int var2, String var3, SelectionListener var4) {
      Button var5 = new Button(var1, 0);
      var5.setLayoutData(new GridData(var2));
      var5.setText(var3);
      var5.addSelectionListener(var4);
   }
}
