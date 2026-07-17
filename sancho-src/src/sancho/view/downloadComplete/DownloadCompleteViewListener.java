package sancho.view.downloadComplete;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewFrame.ViewListener;

public class DownloadCompleteViewListener extends ViewListener {
   public DownloadCompleteViewListener(ViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
   }
}
