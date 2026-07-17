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
   public ResultViewListener(CTabFolderViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.cTabFolder != null) {
         CTabItem[] var2 = this.cTabFolder.getItems();

         for (int var3 = 0; var3 < var2.length; var3++) {
            if (var2[var3].getData("gView") != null) {
               var1.add(new ColumnSelectorAction(this.cTabFolder));
               break;
            }
         }

         if (this.cTabFolder.getSelection() != null && this.cTabFolder.getSelection().getData("gView") != null) {
            this.gView = (GView)this.cTabFolder.getSelection().getData("gView");
            var1.add(new Separator());
            this.createDynamicColumnSubMenu(var1);
            this.createSortByColumnSubMenu(var1);
            MyMenuManager var4 = new MyMenuManager(SResources.getString("mi.show"));
            var4.setImageString("target");
            var4.add(new RemoveAllFiltersAction(this.gView));
            var4.add(new Separator());
            this.createEnabledNetworkFilterSubMenu(var4);
            var4.add(new Separator());
            this.createStateFilterMenuItems(var4);
            var1.add(var4);
         }

         this.createSashActions(var1, "tab.search");
      }
   }
}
