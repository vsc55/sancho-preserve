package sancho.view.friends.clientFiles;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;
import sancho.view.viewer.actions.ColumnSelectorAction;

public class ClientFilesViewListener extends SashViewListener {
   public ClientFilesViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      menuManager.add(new ColumnSelectorAction(this.gView));
      menuManager.add(new Separator());
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
      this.createSashActions(menuManager, "l.clientDirectories");
   }
}
