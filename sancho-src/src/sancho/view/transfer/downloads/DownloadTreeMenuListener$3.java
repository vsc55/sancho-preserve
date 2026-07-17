package sancho.view.transfer.downloads;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Scale;

class DownloadTreeMenuListener$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final Scale val$scale;
   // $VF: synthetic field
   private final DownloadTreeMenuListener$PriorityInputDialog this$0;

   DownloadTreeMenuListener$3(DownloadTreeMenuListener$PriorityInputDialog var1, Scale var2) {
      this.this$0 = var1;
      this.val$scale = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2 = this.val$scale.getSelection() - 100;
      this.this$0.spinner.setSelection(var2);
   }
}
