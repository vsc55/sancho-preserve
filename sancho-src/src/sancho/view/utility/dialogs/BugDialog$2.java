package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class BugDialog$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final BugDialog this$0;

   BugDialog$2(BugDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.close();
   }
}
