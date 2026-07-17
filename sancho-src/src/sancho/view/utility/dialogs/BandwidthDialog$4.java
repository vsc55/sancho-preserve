package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class BandwidthDialog$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final BandwidthDialog$ConfigOptionsDialog this$0;

   BandwidthDialog$4(BandwidthDialog$ConfigOptionsDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (this.this$0.rightList.getSelectionCount() > 0) {
         this.this$0.rightList.remove(this.this$0.rightList.getSelectionIndices());
      }
   }
}
