package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

class BandwidthDialog$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final List val$leftList;
   // $VF: synthetic field
   private final BandwidthDialog$ConfigOptionsDialog this$0;

   BandwidthDialog$5(BandwidthDialog$ConfigOptionsDialog var1, List var2) {
      this.this$0 = var1;
      this.val$leftList = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (this.val$leftList.getSelectionCount() > 0) {
         String[] var2 = this.val$leftList.getSelection();

         for (int var3 = 0; var2 != null && var3 < var2.length; var3++) {
            this.this$0.rightList.add(var2[var3]);
         }
      }
   }
}
