package sancho.view.utility.setupWizard;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class HostPage$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final HostPage this$0;

   HostPage$5(HostPage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (this.this$0.list.getSelectionIndex() != 0) {
         this.this$0.makeDefault();
      }
   }
}
