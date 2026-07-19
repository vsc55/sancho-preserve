package sancho.view.statistics.networks;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class NetworksViewListener extends SashViewListener {
   public NetworksViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      this.createSashActions(menuManager, "tab.statistics");
   }
}
