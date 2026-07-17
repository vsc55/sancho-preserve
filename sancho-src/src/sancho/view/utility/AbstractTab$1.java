package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class AbstractTab$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final AbstractTab this$0;

   AbstractTab$1(AbstractTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (!this.this$0.isActive()) {
         this.this$0.setActive();
      }
   }
}
