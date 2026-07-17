package sancho.view.statusline;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.view.statusline.actions.NetworkConnectMoreAction;
import sancho.view.statusline.actions.NetworkDisableAction;
import sancho.view.statusline.actions.NetworkEnableAction;

class NetworkItem$NetworkMenuListener implements IMenuListener {
   EnumNetwork enumNetwork;

   public NetworkItem$NetworkMenuListener(EnumNetwork var1) {
      this.enumNetwork = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (Sancho.hasCollectionFactory()) {
         Network var2 = Sancho.getCore().getNetworkCollection().getByEnum(this.enumNetwork);
         if (var2 != null) {
            if (!var2.isVirtual()) {
               if (var2.isEnabled()) {
                  var1.add(new NetworkDisableAction(var2.getEnumNetwork()));
               } else {
                  var1.add(new NetworkEnableAction(var2.getEnumNetwork()));
               }
            }

            if (var2.isEnabled() && (var2.hasServers() || var2.hasSupernodes())) {
               var1.add(new Separator());
               var1.add(new NetworkConnectMoreAction(var2.getEnumNetwork()));
            }
         }
      }
   }
}
