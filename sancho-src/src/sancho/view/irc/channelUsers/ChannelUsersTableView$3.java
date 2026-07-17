package sancho.view.irc.channelUsers;

import org.eclipse.jface.viewers.TableViewer;

class ChannelUsersTableView$3 implements Runnable {
   // $VF: synthetic field
   private final Object val$object;
   // $VF: synthetic field
   private final ChannelUsersTableView this$0;

   ChannelUsersTableView$3(ChannelUsersTableView var1, Object var2) {
      this.this$0 = var1;
      this.val$object = var2;
   }

   public void run() {
      ((TableViewer)ChannelUsersTableView.access$200(this.this$0)).remove(this.val$object);
      this.this$0.updateHeader();
   }
}
