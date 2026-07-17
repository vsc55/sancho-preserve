package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.MainWindow;

class HeaderBarMouseAdapter$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final HeaderBarMouseAdapter$1 this$1;

   HeaderBarMouseAdapter$2(HeaderBarMouseAdapter$1 var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      MainWindow.copyToClipboard(HeaderBarMouseAdapter.access$000(HeaderBarMouseAdapter$1.access$100(this.this$1)).getText());
   }
}
