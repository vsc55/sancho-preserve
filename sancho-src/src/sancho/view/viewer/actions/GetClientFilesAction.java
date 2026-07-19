package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class GetClientFilesAction extends Action {
   Client[] clientArray;

   public GetClientFilesAction(Client[] clients) {
      super(SResources.getString("mi.getClientFiles"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      this.clientArray = clients;
   }

   public void run() {
      for (int i = 0; i < this.clientArray.length; i++) {
         if (this.clientArray[i] != null) {
            this.clientArray[i].requestClientFiles();
         }
      }
   }
}
