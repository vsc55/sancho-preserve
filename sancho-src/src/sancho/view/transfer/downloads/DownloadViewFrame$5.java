package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class DownloadViewFrame$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final DownloadViewFrame this$0;

   DownloadViewFrame$5(DownloadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      ((CustomTreeViewer)DownloadViewFrame.access$200(this.this$0).getViewer()).expandAll();
   }
}
