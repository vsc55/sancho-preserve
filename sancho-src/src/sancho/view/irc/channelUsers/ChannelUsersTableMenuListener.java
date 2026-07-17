package sancho.view.irc.channelUsers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.irc.CUser;
import sancho.view.irc.IRCClient;
import sancho.view.viewer.table.GTableMenuListener;

public class ChannelUsersTableMenuListener extends GTableMenuListener {
   protected CUser selectedCUser;
   IRCClient ircClient;

   public ChannelUsersTableMenuListener(ChannelUsersTableView var1) {
      super(var1);
   }

   public void deselectAll() {
      this.gView.deselectAll();
      this.selectedCUser = null;
   }

   public void setIRCClient(IRCClient var1) {
      this.ircClient = var1;
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      IStructuredSelection var2 = (IStructuredSelection)var1.getSelection();
      this.selectedCUser = (CUser)var2.getFirstElement();
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedCUser != null) {
         var1.add(new ChannelUsersTableMenuListener$WhoisAction(this));
      }
   }
}
