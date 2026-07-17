package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class LinkEntry$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final LinkEntry this$0;

   LinkEntry$2(LinkEntry var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      LinkEntry.access$000(this.this$0).getMainWindow().sendTorrentsFromHD();
   }
}
