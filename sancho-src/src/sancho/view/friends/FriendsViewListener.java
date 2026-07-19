package sancho.view.friends;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewFrame.TabbedSashViewListener;
import sancho.view.viewFrame.TabbedViewFrame;
import sancho.view.viewFrame.actions.ToggleTabsAction;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class FriendsViewListener extends TabbedSashViewListener {
   public FriendsViewListener(TabbedSashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      MyMenuManager showMenuManager = new MyMenuManager(SResources.getString("mi.show"));
      showMenuManager.setImageString("target");
      showMenuManager.add(new RemoveAllFiltersAction(this.gView));
      showMenuManager.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(showMenuManager);
      showMenuManager.add(new Separator());
      this.createStateFilterMenuItems(showMenuManager);
      menuManager.add(showMenuManager);
      menuManager.add(new ToggleTabsAction((TabbedViewFrame)this.viewFrame));
      this.createSashActions(menuManager, "messages");
   }
}
