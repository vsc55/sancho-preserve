package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.view.transfer.ClientDetailDialog;
import sancho.view.utility.SResources;

public class ClientDetailAction extends Action {
   private File file;
   private Client client;
   private Shell parentShell;

   public ClientDetailAction(Shell parentShell, File file, Client client) {
      super(SResources.getString("m.d.clientDetails"));
      this.setImageDescriptor(SResources.getImageDescriptor("info"));
      this.parentShell = parentShell;
      this.file = file;
      this.client = client;
   }

   public void run() {
      new ClientDetailDialog(this.parentShell, this.file, this.client).open();
   }
}
