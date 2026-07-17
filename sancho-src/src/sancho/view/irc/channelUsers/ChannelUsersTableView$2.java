package sancho.view.irc.channelUsers;

import org.eclipse.jface.viewers.TableViewer;

class ChannelUsersTableView$2 implements Runnable {
   // $VF: synthetic field
   private final Object val$object;
   // $VF: synthetic field
   private final ChannelUsersTableView this$0;

   ChannelUsersTableView$2(ChannelUsersTableView var1, Object var2) {
      this.this$0 = var1;
      this.val$object = var2;
   }

   public void run() {
      ((TableViewer)ChannelUsersTableView.access$100(this.this$0)).add(this.val$object);
      this.this$0.updateHeader();
   }
}
