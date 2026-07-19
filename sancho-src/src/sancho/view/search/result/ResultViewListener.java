package sancho.view.search.result;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.utility.MyMenuManager;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.CTabFolderViewFrame;
import sancho.view.viewFrame.CTabFolderViewListener;
import sancho.view.viewer.GView;
import sancho.view.viewer.actions.ColumnSelectorAction;
import sancho.view.viewer.actions.RemoveAllFiltersAction;

public class ResultViewListener extends CTabFolderViewListener {
   public ResultViewListener(CTabFolderViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager manager) {
      if (this.cTabFolder != null) {
         CTabItem[] items = this.cTabFolder.getItems();

         for (int i = 0; i < items.length; i++) {
            if (items[i].getData("gView") != null) {
               manager.add(new ColumnSelectorAction(this.cTabFolder));
               break;
            }
         }

         if (this.cTabFolder.getSelection() != null && this.cTabFolder.getSelection().getData("gView") != null) {
            this.gView = (GView)this.cTabFolder.getSelection().getData("gView");
            manager.add(new Separator());
            this.createDynamicColumnSubMenu(manager);
            this.createSortByColumnSubMenu(manager);
            MyMenuManager showMenu = new MyMenuManager(SResources.getString("mi.show"));
            showMenu.setImageString("target");
            showMenu.add(new RemoveAllFiltersAction(this.gView));
            showMenu.add(new Separator());
            this.createEnabledNetworkFilterSubMenu(showMenu);
            showMenu.add(new Separator());
            this.createStateFilterMenuItems(showMenu);
            manager.add(showMenu);
         }

         this.createSashActions(manager, "tab.search");
      }
   }
}
