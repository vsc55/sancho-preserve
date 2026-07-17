package sancho.view.friends.clientDirectories;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class ClientDirectoriesViewListener extends SashViewListener {
   public ClientDirectoriesViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createSashActions(var1, "l.clientFiles");
   }
}
