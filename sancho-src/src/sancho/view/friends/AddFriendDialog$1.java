package sancho.view.friends;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class AddFriendDialog$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final AddFriendDialog this$0;

   AddFriendDialog$1(AddFriendDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      String var2 = "afr " + AddFriendDialog.access$000(this.this$0).getText() + " " + AddFriendDialog.access$100(this.this$0).getText();
      Sancho.send((short)29, var2);
      this.this$0.close();
   }
}
