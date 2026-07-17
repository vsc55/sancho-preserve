package sancho.view.transfer.downloads;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class DownloadTreeLabelProvider$1 implements Listener {
   // $VF: synthetic field
   private final DownloadTreeLabelProvider this$0;

   DownloadTreeLabelProvider$1(DownloadTreeLabelProvider var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      switch (DownloadTreeLabelProvider.access$000(this.this$0).getColumnIDs()[var1.index]) {
         case 9:
            DownloadTreeLabelProvider.access$100(this.this$0, var1);
      }
   }
}
