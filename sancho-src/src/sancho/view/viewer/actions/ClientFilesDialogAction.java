package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.view.transfer.ClientFilesDialog;
import sancho.view.utility.SResources;

public class ClientFilesDialogAction extends Action {
   Shell shell;
   Client client;

   public ClientFilesDialogAction(Shell shell, Client client) {
      super(SResources.getString("mi.viewClientFiles"));
      this.setImageDescriptor(SResources.getImageDescriptor("search_small"));
      this.shell = shell;
      this.client = client;
   }

   public void run() {
      new ClientFilesDialog(this.shell, this.client).open();
   }
}
