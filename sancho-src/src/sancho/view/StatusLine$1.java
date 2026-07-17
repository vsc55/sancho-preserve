package sancho.view;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import sancho.view.utility.SResources;

class StatusLine$1 extends MouseAdapter {
   // $VF: synthetic field
   private final StatusLine this$0;

   StatusLine$1(StatusLine var1) {
      this.this$0 = var1;
   }

   public void mouseUp(MouseEvent var1) {
      CLabel var2 = (CLabel)var1.widget;
      if (var2.getText().equals(SResources.getString("l.newMessage"))) {
         this.this$0.getMainWindow().switchToFriends();
      }
   }
}
