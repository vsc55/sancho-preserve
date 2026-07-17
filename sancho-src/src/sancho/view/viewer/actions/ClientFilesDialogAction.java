package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.view.transfer.ClientFilesDialog;
import sancho.view.utility.SResources;

public class ClientFilesDialogAction extends Action {
   Shell shell;
   Client client;

   public ClientFilesDialogAction(Shell var1, Client var2) {
      super(SResources.getString("mi.viewClientFiles"));
      this.setImageDescriptor(SResources.getImageDescriptor("search_small"));
      this.shell = var1;
      this.client = var2;
   }

   public void run() {
      new ClientFilesDialog(this.shell, this.client).open();
   }
}
