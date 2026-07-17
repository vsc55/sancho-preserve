package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class IDSelector$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final IDSelector this$0;

   IDSelector$3(IDSelector var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2;
      if ((var2 = IDSelector.access$300(this.this$0).getSelectionIndex()) > 0) {
         this.this$0.moveItem(var2, -1);
      }
   }
}
