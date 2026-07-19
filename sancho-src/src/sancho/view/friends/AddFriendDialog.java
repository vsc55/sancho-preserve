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

   public AddFriendDialog(Shell shell) {
      super(shell);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
      shell.setText(SResources.getString("t.f.addByIP"));
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 10, 5, false));
      Label hostLabel = new Label(composite, 0);
      hostLabel.setLayoutData(new GridData(128));
      hostLabel.setText(SResources.getString("t.f.host"));
      this.host = new Text(composite, 2048);
      this.host.setText("192.168.1.1");
      GridData gridData = new GridData(768);
      gridData.widthHint = 120;
      this.host.setLayoutData(gridData);
      Label portLabel = new Label(composite, 0);
      portLabel.setLayoutData(new GridData(128));
      portLabel.setText(SResources.getString("t.f.port"));
      this.port = new Text(composite, 2048);
      this.port.setText("4662");
      this.port.setLayoutData(new GridData(768));
      return composite;
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayoutData(new GridData(768));
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button okButton = new Button(composite, 0);
      okButton.setLayoutData(new GridData(768));
      okButton.setText(SResources.getString("b.ok"));
      okButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            String command = "afr " + AddFriendDialog.this.host.getText() + " " + AddFriendDialog.this.port.getText();
            Sancho.send((short)29, command);
            AddFriendDialog.this.close();
         }
      });
      Button cancelButton = new Button(composite, 0);
      cancelButton.setLayoutData(new GridData(768));
      cancelButton.setText(SResources.getString("b.cancel"));
      cancelButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            AddFriendDialog.this.close();
         }
      });
      // Make OK the default button so Enter in the host/port fields submits (the
      // hand-rolled button bar set no default button, so Enter did nothing).
      composite.getShell().setDefaultButton(okButton);
      return composite;
   }
}
