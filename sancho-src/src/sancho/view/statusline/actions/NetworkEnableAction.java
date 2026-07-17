package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.utility.SResources;

public class NetworkEnableAction extends Action {
   EnumNetwork enumNetwork;

   public NetworkEnableAction(EnumNetwork var1) {
      super(SResources.getString("sl.n.enable"));
      this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
      this.enumNetwork = var1;
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         Network var1 = Sancho.getCore().getNetworkCollection().getByEnum(this.enumNetwork);
         if (var1 != null) {
            var1.toggleEnabled();
         }
      }
   }
}
