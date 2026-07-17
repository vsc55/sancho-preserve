package sancho.view.utility.setupWizard;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class HostPage$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final HostPage this$0;

   HostPage$1(HostPage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.resetInfo();
   }
}
