package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolItem;

class LinkEntryItem$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final ToolItem val$toolItem;
   // $VF: synthetic field
   private final LinkEntryItem this$0;

   LinkEntryItem$3(LinkEntryItem var1, ToolItem var2) {
      this.this$0 = var1;
      this.val$toolItem = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      GridData var2 = new GridData(768);
      if (this.this$0.linkEntryToggle) {
         this.val$toolItem.setSelection(false);
         var2.heightHint = 0;
      } else {
         this.val$toolItem.setSelection(true);
         var2.heightHint = 75;
      }

      this.this$0.linkEntryToggle = !this.this$0.linkEntryToggle;
      this.this$0.linkEntryComposite.setLayoutData(var2);
      this.this$0.statusLine.getMainWindow().getMainComposite().layout();
   }
}
