package sancho.view.transfer.fileComments;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;

public class FileCommentsViewListener extends ViewListener {
   public FileCommentsViewListener(ViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
   }
}
