package sancho.view.statistics.networks;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class NetworksViewListener extends SashViewListener {
   public NetworksViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      this.createSashActions(var1, "tab.statistics");
   }
}
