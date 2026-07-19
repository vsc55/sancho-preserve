package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.utility.SResources;

public class NetworkConnectMoreAction extends Action {
   public NetworkConnectMoreAction(EnumNetwork enumNetwork) {
      super(SResources.getString("sl.n.connect"));
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         Sancho.getCore().getServerCollection().connectMore();
      }
   }
}
