package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class DisconnectClientAction extends Action {
   Client[] clientArray;

   public DisconnectClientAction(Client[] var1) {
      super(SResources.getString("mi.disconnectClient"));
      this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
      this.clientArray = var1;
   }

   public void run() {
      for (int var1 = 0; var1 < this.clientArray.length; var1++) {
         this.clientArray[var1].disconnect();
      }
   }
}
