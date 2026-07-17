package sancho.view.console;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.MainWindow;

class Console$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final Console this$0;

   Console$5(Console var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      MainWindow.copyToClipboard(this.this$0.infoDisplay.getSelectionText());
   }
}
