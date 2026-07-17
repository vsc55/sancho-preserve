package sancho.view.irc;

import java.util.List;
import sancho.view.console.IRCConsole;
import sancho.view.irc.channelUsers.ChannelUsersTableView;

class IRCShell$IRCTab {
   IRCConsole console;
   ChannelUsersTableView channelUsersTableView;
   IRCConsoleViewFrame ircConsoleViewFrame;
   List cUsers;

   public IRCConsole getConsole() {
      return this.console;
   }

   public void setView(ChannelUsersTableView var1) {
      this.channelUsersTableView = var1;
   }

   public ChannelUsersTableView getView() {
      return this.channelUsersTableView;
   }

   public IRCConsoleViewFrame getConsoleFrame() {
      return this.ircConsoleViewFrame;
   }

   public void setConsoleFrame(IRCConsoleViewFrame var1) {
      this.ircConsoleViewFrame = var1;
      this.console = var1.getConsole();
   }

   public void setCUsers(List var1) {
      this.cUsers = var1;
   }

   public List getCUsers() {
      return this.cUsers;
   }

   public void dispose() {
      this.cUsers.clear();
      this.console.dispose();
   }
}
