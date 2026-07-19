package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class DisconnectClientAction extends Action {
   Client[] clientArray;

   public DisconnectClientAction(Client[] clients) {
      super(SResources.getString("mi.disconnectClient"));
      this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
      this.clientArray = clients;
   }

   public void run() {
      for (int i = 0; i < this.clientArray.length; i++) {
         this.clientArray[i].disconnect();
      }
   }
}
