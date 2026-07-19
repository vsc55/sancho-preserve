package sancho.view.transfer.fileDialog;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;

public class SubfilesViewListener extends ViewListener {
   public SubfilesViewListener(ViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      this.createDynamicColumnSubMenu(menuManager);
      this.createSortByColumnSubMenu(menuManager);
   }
}
