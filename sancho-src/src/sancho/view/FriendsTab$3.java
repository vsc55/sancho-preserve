package sancho.view;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.console.MessageConsole;

class FriendsTab$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final FriendsTab this$0;

   FriendsTab$3(FriendsTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      MessageConsole var3 = (MessageConsole)var2.getData("messageConsole");
      this.this$0.setTabsLabel();
      var3.setFocus();
   }
}
