package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class CoreVerbosityDialog$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final CoreVerbosityDialog this$0;

   CoreVerbosityDialog$1(CoreVerbosityDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      for (int var2 = 0; var2 < this.this$0.buttonArray.length; var2++) {
         if (this.this$0.buttonArray[var2] != null) {
            this.this$0.buttonArray[var2].setSelection(false);
         }
      }
   }
}
