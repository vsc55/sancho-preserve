package sancho.view.irc.channelUsers;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class ChannelUsersViewListener extends SashViewListener {
   public ChannelUsersViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      this.createSashActions(var1, "l.IRC");
   }
}
