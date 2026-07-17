package sancho.view;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class RoomsTab$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final RoomsTab this$0;

   RoomsTab$2(RoomsTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.setCTabFolderSelection((CTabItem)var1.item);
   }
}
