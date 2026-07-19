package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.utility.SResources;

public class NetworkDisableAction extends Action {
   EnumNetwork enumNetwork;

   public NetworkDisableAction(EnumNetwork enumNetwork) {
      super(SResources.getString("sl.n.disable"));
      this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
      this.enumNetwork = enumNetwork;
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         Network network = Sancho.getCore().getNetworkCollection().getByEnum(this.enumNetwork);
         if (network != null) {
            network.toggleEnabled();
         }
      }
   }
}
