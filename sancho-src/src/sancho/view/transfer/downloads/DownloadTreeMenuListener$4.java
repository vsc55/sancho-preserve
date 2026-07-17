package sancho.view.transfer.downloads;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class DownloadTreeMenuListener$4 implements Listener {
   // $VF: synthetic field
   private final DownloadTreeMenuListener$PriorityInputDialog this$0;

   DownloadTreeMenuListener$4(DownloadTreeMenuListener$PriorityInputDialog var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if (var1.detail != 2 && var1.detail == 4) {
         this.this$0.onClose();
         this.this$0.close();
      }
   }
}
