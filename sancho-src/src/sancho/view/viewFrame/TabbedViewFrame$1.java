package sancho.view.viewFrame;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class TabbedViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final TabbedViewFrame this$0;

   TabbedViewFrame$1(TabbedViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      this.this$0.oldSelectionItem.setData("filterString", this.this$0.gView.filtersToString());
      this.this$0.oldSelectionItem.setControl(null);
      this.this$0.switchToTab(var2, false);
   }
}
