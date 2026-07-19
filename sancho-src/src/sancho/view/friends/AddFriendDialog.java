package sancho.view.friends;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class AddFriendDialog extends Dialog {
   private Text host;
   private Text port;

   public AddFriendDialog(Shell var1) {
      super(var1);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(SResources.getString("t.f.addByIP"));
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
      Label var3 = new Label(var2, 0);
      var3.setLayoutData(new GridData(128));
      var3.setText(SResources.getString("t.f.host"));
      this.host = new Text(var2, 2048);
      this.host.setText("192.168.1.1");
      GridData var4 = new GridData(768);
      var4.widthHint = 120;
      this.host.setLayoutData(var4);
      Label var5 = new Label(var2, 0);
      var5.setLayoutData(new GridData(128));
      var5.setText(SResources.getString("t.f.port"));
      this.port = new Text(var2, 2048);
      this.port.setText("4662");
      this.port.setLayoutData(new GridData(768));
      return var2;
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayoutData(new GridData(768));
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(768));
      var3.setText(SResources.getString("b.ok"));
      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            String var2 = "afr " + AddFriendDialog.this.host.getText() + " " + AddFriendDialog.this.port.getText();
            Sancho.send((short)29, var2);
            AddFriendDialog.this.close();
         }
      });
      Button var4 = new Button(var2, 0);
      var4.setLayoutData(new GridData(768));
      var4.setText(SResources.getString("b.cancel"));
      var4.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            AddFriendDialog.this.close();
         }
      });
      // Make OK the default button so Enter in the host/port fields submits (the
      // hand-rolled button bar set no default button, so Enter did nothing).
      var2.getShell().setDefaultButton(var3);
      return var2;
   }
}
