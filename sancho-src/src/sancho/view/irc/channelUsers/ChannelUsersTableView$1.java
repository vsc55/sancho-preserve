package sancho.view.irc.channelUsers;

import java.util.List;

class ChannelUsersTableView$1 implements Runnable {
   // $VF: synthetic field
   private final List val$arrayList;
   // $VF: synthetic field
   private final ChannelUsersTableView this$0;

   ChannelUsersTableView$1(ChannelUsersTableView var1, List var2) {
      this.this$0 = var1;
      this.val$arrayList = var2;
   }

   public void run() {
      ChannelUsersTableView.access$000(this.this$0).setInput(this.val$arrayList);
      this.this$0.updateHeader();
   }
}
