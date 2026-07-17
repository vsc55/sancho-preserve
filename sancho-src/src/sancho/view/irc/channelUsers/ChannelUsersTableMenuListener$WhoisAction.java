package sancho.view.irc.channelUsers;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class ChannelUsersTableMenuListener$WhoisAction extends Action {
   // $VF: synthetic field
   private final ChannelUsersTableMenuListener this$0;

   public ChannelUsersTableMenuListener$WhoisAction(ChannelUsersTableMenuListener var1) {
      super(SResources.getString("whois"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
   }

   public void run() {
      this.this$0.ircClient.whois(this.this$0.selectedCUser.getNick());
   }
}
