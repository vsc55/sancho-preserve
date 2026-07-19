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

   public ClientFilesDialog(Shell shell, Client client) {
      super(shell);
      this.client = client;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
      shell.setText(this.client.getId() + ": " + this.client.getName());
   }

   protected int getShellStyle() {
      return super.getShellStyle() | 16;
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(new FillLayout());
      String sashName = "clientFilesSash";
      SashForm sashForm = WidgetFactory.createSashForm(composite, sashName);
      ClientDirectoriesViewFrame directoriesFrame = new ClientDirectoriesViewFrame(sashForm, "l.clientDirectories", "tab.friends.buttonSmall", null);
      ClientFilesViewFrame filesFrame = new ClientFilesViewFrame(sashForm, "l.clientFiles", "tab.friends.buttonSmall", null);
      ((ClientDirectoriesTableView)directoriesFrame.getGView()).setFilesView((ClientFilesTableView)filesFrame.getGView());
      ((ClientDirectoriesTableView)directoriesFrame.getGView()).setInput(this.client);
      WidgetFactory.loadSashForm(sashForm, sashName);
      return composite;
   }

   protected Control createButtonBar(Composite parent) {
      Composite composite = new Composite(parent, 0);
      composite.setLayoutData(new GridData(768));
      composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 0, false));
      Button addFriendButton = new Button(composite, 0);
      addFriendButton.setLayoutData(new GridData(640));
      addFriendButton.setText(SResources.getString("dd.c.addFriend"));
      if (this.client.getEnumClientType() == EnumClientType.FRIEND) {
         addFriendButton.setEnabled(false);
      }

      addFriendButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ClientFilesDialog.this.client.addAsFriend();
            addFriendButton.setText(SResources.getString("b.ok"));
            addFriendButton.setEnabled(false);
         }
      });
      Button closeButton = new Button(composite, 0);
      closeButton.setFocus();
      closeButton.setLayoutData(new GridData(128));
      closeButton.setText(SResources.getString("b.close"));
      closeButton.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            ClientFilesDialog.this.close();
         }
      });
      return composite;
   }
}
