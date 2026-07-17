package sancho.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class FriendsTab$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final FriendsTab$MessagesViewFrame this$1;

   FriendsTab$5(FriendsTab$MessagesViewFrame var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      FriendsTab$MessagesViewFrame.access$100(this.this$1).closeAllTabs();
   }
}
