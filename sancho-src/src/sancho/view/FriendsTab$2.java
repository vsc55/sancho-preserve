package sancho.view;

import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.console.MessageConsole;

class FriendsTab$2 extends CTabFolder2Adapter {
   // $VF: synthetic field
   private final FriendsTab this$0;

   FriendsTab$2(FriendsTab var1) {
      this.this$0 = var1;
   }

   public void close(CTabFolderEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      MessageConsole var3 = (MessageConsole)var2.getData("messageConsole");
      Integer var4 = (Integer)var2.getData("id");
      FriendsTab.access$000(this.this$0).remove(var4);
      var3.dispose();
      var2.dispose();
      this.this$0.setTabsLabel();
   }
}
