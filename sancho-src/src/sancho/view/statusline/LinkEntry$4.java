package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

class LinkEntry$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final Text val$linkEntryText;
   // $VF: synthetic field
   private final LinkEntry this$0;

   LinkEntry$4(LinkEntry var1, Text var2) {
      this.this$0 = var1;
      this.val$linkEntryText = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.enterLinks(this.val$linkEntryText);
   }
}
