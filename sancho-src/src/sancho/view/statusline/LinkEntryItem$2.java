package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.utility.LinkRipper;

class LinkEntryItem$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final LinkEntryItem this$0;

   LinkEntryItem$2(LinkEntryItem var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      LinkRipper var2 = this.this$0.statusLine.getMainWindow().getLinkRipper();
      if (var2 != null) {
         var2.setFocus();
      } else {
         var2 = this.this$0.statusLine.getMainWindow().openLinkRipper();
         this.this$0.setupLinkRipper(var2);
         var2.open();
      }
   }
}
