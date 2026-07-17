package sancho.view.transfer.downloads;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class DownloadViewFrame$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final DownloadViewFrame this$0;

   DownloadViewFrame$3(DownloadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      ((DownloadTreeView)DownloadViewFrame.access$000(this.this$0)).toggleClientsTable();
   }
}
