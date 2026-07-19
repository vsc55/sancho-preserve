package sancho.view.transfer.clients;

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

public class ClientViewListener extends TabbedSashViewListener {
   public ClientViewListener(TabbedSashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menu) {
      menu.add(new ColumnSelectorAction(this.gView));
      menu.add(new Separator());
      this.createDynamicColumnSubMenu(menu);
      this.createSortByColumnSubMenu(menu);
      MyMenuManager showMenu = new MyMenuManager(SResources.getString("mi.show"));
      showMenu.setImageString("target");
      showMenu.add(new RemoveAllFiltersAction(this.gView));
      showMenu.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(showMenu);
      showMenu.add(new Separator());
      this.createStateFilterMenuItems(showMenu);
      menu.add(showMenu);
      menu.add(new ToggleTabsAction((TabbedViewFrame)this.viewFrame));
      this.createSashActions(menu, "l.downloads");
   }
}
