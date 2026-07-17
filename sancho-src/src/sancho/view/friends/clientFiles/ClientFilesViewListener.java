package sancho.view.friends.clientFiles;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;
import sancho.view.viewer.actions.ColumnSelectorAction;

public class ClientFilesViewListener extends SashViewListener {
   public ClientFilesViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ColumnSelectorAction(this.gView));
      var1.add(new Separator());
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      this.createSashActions(var1, "l.clientDirectories");
   }
}
