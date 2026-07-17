package sancho.view.transfer.downloads;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Scale;

class DownloadTreeMenuListener$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final Scale val$scale2;
   // $VF: synthetic field
   private final DownloadTreeMenuListener$PriorityInputDialog this$0;

   DownloadTreeMenuListener$5(DownloadTreeMenuListener$PriorityInputDialog var1, Scale var2) {
      this.this$0 = var1;
      this.val$scale2 = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2 = this.val$scale2.getSelection() - 20;
      this.this$0.spinner2.setSelection(var2);
   }
}
