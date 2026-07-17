package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ToolItem;

class LinkEntryItem$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final ToolItem val$cToolItem;
   // $VF: synthetic field
   private final LinkEntryItem this$0;

   LinkEntryItem$4(LinkEntryItem var1, ToolItem var2) {
      this.this$0 = var1;
      this.val$cToolItem = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.statusConsole.toggleVisible();
      this.val$cToolItem.setSelection(this.this$0.statusConsole.isVisible());
   }
}
