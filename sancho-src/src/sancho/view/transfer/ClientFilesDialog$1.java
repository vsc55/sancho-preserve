package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import sancho.view.utility.SResources;

class ClientFilesDialog$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final Button val$addFriendButton;
   // $VF: synthetic field
   private final ClientFilesDialog this$0;

   ClientFilesDialog$1(ClientFilesDialog var1, Button var2) {
      this.this$0 = var1;
      this.val$addFriendButton = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.client.addAsFriend();
      this.val$addFriendButton.setText(SResources.getString("b.ok"));
      this.val$addFriendButton.setEnabled(false);
   }
}
