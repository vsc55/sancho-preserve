package sancho.view.server;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewFrame.TabbedSashViewListener;
import sancho.view.viewFrame.actions.ToggleTabsAction;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class ServerViewListener extends TabbedSashViewListener {
   public ServerViewListener(TabbedSashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager manager) {
      manager.add(new ColumnSelectorAction(this.gView));
      manager.add(new Separator());
      this.createDynamicColumnSubMenu(manager);
      this.createSortByColumnSubMenu(manager);
      MyMenuManager subMenu = new MyMenuManager(SResources.getString("mi.show"));
      subMenu.setImageString("target");
      subMenu.add(new RemoveAllFiltersAction(this.gView));
      subMenu.add(new Separator());
      this.createNetworkWithServersFilterSubMenu(subMenu);
      subMenu.add(new Separator());
      this.createStateFilterMenuItems(subMenu);
      manager.add(subMenu);
      manager.add(new ToggleTabsAction((TabbedSashViewFrame)this.viewFrame));
      this.createSashActions(manager, "l.serverUsers");
   }
}
