package sancho.view.irc.channelUsers;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class ChannelUsersViewFrame extends SashViewFrame {
   public ChannelUsersViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new ChannelUsersTableView(this);
      this.createViewListener(new ChannelUsersViewListener(this));
   }
}
