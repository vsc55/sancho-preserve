package sancho.view.transfer.downloads;

import org.eclipse.jface.viewers.CustomTreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class DownloadViewFrame$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final DownloadViewFrame this$0;

   DownloadViewFrame$4(DownloadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      ((CustomTreeViewer)DownloadViewFrame.access$100(this.this$0).getViewer()).collapseAll();
   }
}
