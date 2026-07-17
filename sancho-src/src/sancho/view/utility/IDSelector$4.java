package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class IDSelector$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final IDSelector this$0;

   IDSelector$4(IDSelector var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2 = IDSelector.access$300(this.this$0).getSelectionIndex();
      if (var2 < IDSelector.access$300(this.this$0).getItemCount() - 1 && var2 > -1) {
         this.this$0.moveItem(var2, 1);
      }
   }
}
