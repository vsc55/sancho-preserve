package sancho.view.transfer.pending;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class PendingViewListener extends SashViewListener {
   public PendingViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      MyMenuManager subMenuManager = new MyMenuManager(SResources.getString("mi.show"));
      subMenuManager.setImageString("target");
      subMenuManager.add(new RemoveAllFiltersAction(this.gView));
      subMenuManager.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(subMenuManager);
      menuManager.add(subMenuManager);
      this.createSashActions(menuManager, "l.uploaders");
   }
}
