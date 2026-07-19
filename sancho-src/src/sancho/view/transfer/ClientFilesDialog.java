package sancho.view.transfer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.enums.EnumClientType;
import sancho.utility.VersionInfo;
import sancho.view.friends.clientDirectories.ClientDirectoriesTableView;
import sancho.view.friends.clientDirectories.ClientDirectoriesViewFrame;
import sancho.view.friends.clientFiles.ClientFilesTableView;
import sancho.view.friends.clientFiles.ClientFilesViewFrame;
import sancho.view.utility.SResources;
import sancho.view.utility.WidgetFactory;

public class ClientFilesDialog extends Dialog {
   protected Client client;

   public ClientFilesDialog(Shell var1, Client var2) {
      super(var1);
      this.client = var2;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(this.client.getId() + ": " + this.client.getName());
   }

   protected int getShellStyle() {
      return super.getShellStyle() | 16;
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(new FillLayout());
      String var3 = "clientFilesSash";
      SashForm var4 = WidgetFactory.createSashForm(var2, var3);
      ClientDirectoriesViewFrame var5 = new ClientDirectoriesViewFrame(var4, "l.clientDirectories", "tab.friends.buttonSmall", null);
      ClientFilesViewFrame var6 = new ClientFilesViewFrame(var4, "l.clientFiles", "tab.friends.buttonSmall", null);
      ((ClientDirectoriesTableView)var5.getGView()).setFilesView((ClientFilesTableView)var6.getGView());
      ((ClientDirectoriesTableView)var5.getGView()).setInput(this.client);
      WidgetFactory.loadSashForm(var4, var3);
      return var2;
   }

   protected Control createButtonBar(Composite var1) {
      Composite var2 = new Composite(var1, 0);
      var2.setLayoutData(new GridData(768));
      var2.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button var3 = new Button(var2, 0);
      var3.setLayoutData(new GridData(640));
      var3.setText(SResources.getString("dd.c.addFriend"));
      if (this.client.getEnumClientType() == EnumClientType.FRIEND) {
         var3.setEnabled(false);
      }

      var3.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ClientFilesDialog.this.client.addAsFriend();
            var3.setText(SResources.getString("b.ok"));
            var3.setEnabled(false);
         }
      });
      Button var4 = new Button(var2, 0);
      var4.setFocus();
      var4.setLayoutData(new GridData(128));
      var4.setText(SResources.getString("b.close"));
      var4.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            ClientFilesDialog.this.close();
         }
      });
      return var2;
   }
}
