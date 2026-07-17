package sancho.view;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

class FriendsTab$MessagesViewListener extends SashViewListener {
   public FriendsTab$MessagesViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createSashActions(var1, "l.clientFiles");
   }
}
