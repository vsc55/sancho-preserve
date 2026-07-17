package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class LinkRipper$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$3(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.close();
   }
}
