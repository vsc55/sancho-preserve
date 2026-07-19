package sancho.view.rooms;

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

public class RoomsViewListener extends TabbedSashViewListener {
   public RoomsViewListener(TabbedSashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      MyMenuManager showMenu = new MyMenuManager(SResources.getString("mi.show"));
      showMenu.setImageString("target");
      showMenu.add(new RemoveAllFiltersAction(this.gView));
      showMenu.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(showMenu);
      showMenu.add(new Separator());
      this.createStateFilterMenuItems(showMenu);
      menuManager.add(showMenu);
      menuManager.add(new ToggleTabsAction((TabbedViewFrame)this.viewFrame));
      this.createSashActions(menuManager, "l.room");
   }
}
