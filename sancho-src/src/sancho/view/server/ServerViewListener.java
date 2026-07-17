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
   public ServerViewListener(TabbedSashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ColumnSelectorAction(this.gView));
      var1.add(new Separator());
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      MyMenuManager var2 = new MyMenuManager(SResources.getString("mi.show"));
      var2.setImageString("target");
      var2.add(new RemoveAllFiltersAction(this.gView));
      var2.add(new Separator());
      this.createNetworkWithServersFilterSubMenu(var2);
      var2.add(new Separator());
      this.createStateFilterMenuItems(var2);
      var1.add(var2);
      var1.add(new ToggleTabsAction((TabbedSashViewFrame)this.viewFrame));
      this.createSashActions(var1, "l.serverUsers");
   }
}
