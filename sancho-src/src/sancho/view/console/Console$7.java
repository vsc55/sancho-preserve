package sancho.view.console;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class Console$7 extends SelectionAdapter {
   // $VF: synthetic field
   private final Console this$0;

   Console$7(Console var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.infoDisplay.replaceTextRange(0, this.this$0.infoDisplay.getText().length(), "");
   }
}
