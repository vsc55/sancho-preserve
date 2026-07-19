package sancho.view.shares;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;
import sancho.view.viewFrame.actions.ToggleTabsAction;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.RefreshUploadsAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class UploadViewListener extends ViewListener {
   public UploadViewListener(ViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new RefreshUploadsAction());
      menuManager.add(new Separator());
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      MyMenuManager showMenu = new MyMenuManager(SResources.getString("mi.show"));
      showMenu.setImageString("target");
      showMenu.add(new RemoveAllFiltersAction(this.gView));
      showMenu.add(new Separator());
      this.createEnabledNetworkFilterSubMenu(showMenu);
      menuManager.add(showMenu);
      menuManager.add(new ToggleTabsAction((TabbedViewFrame)this.viewFrame));
   }
}
