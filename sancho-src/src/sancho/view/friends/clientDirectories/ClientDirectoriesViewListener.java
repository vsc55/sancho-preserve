package sancho.view.friends.clientDirectories;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class ClientDirectoriesViewListener extends SashViewListener {
   public ClientDirectoriesViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      this.createSashActions(menuManager, "l.clientFiles");
   }
}
