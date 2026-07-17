package sancho.view.transfer.downloads;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class DownloadViewFrame$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final DownloadViewFrame this$0;

   DownloadViewFrame$2(DownloadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         this.this$0.getCore().getFileCollection().commitAll();
      }
   }
}
