package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class ConnectClientAction extends Action {
   Client[] clientArray;

   public ConnectClientAction(Client[] clientArray) {
      super(SResources.getString("mi.connectClient"));
      this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
      this.clientArray = clientArray;
   }

   public void run() {
      for (int i = 0; i < this.clientArray.length; i++) {
         this.clientArray[i].connect();
      }
   }
}
