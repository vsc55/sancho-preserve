package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class IDSelector$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final IDSelector this$0;

   IDSelector$1(IDSelector var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      IDSelector.access$002(this.this$0, IDSelector.access$100(this.this$0));
      IDSelector.access$202(this.this$0, "");
      IDSelector.access$300(this.this$0).removeAll();
      this.this$0.createItems();
   }
}
