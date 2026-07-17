package sancho.view;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

class SearchTab$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final SearchTab this$0;

   SearchTab$1(SearchTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      var2.setControl((Control)var2.getData("myControl"));

      for (int var3 = 0; var3 < SearchTab.access$000(this.this$0).getItems().length; var3++) {
         if (SearchTab.access$000(this.this$0).getItems()[var3] != var2) {
            SearchTab.access$000(this.this$0).getItems()[var3].setControl(null);
         }
      }

      SearchTab.access$000(this.this$0).getParent().layout();
   }
}
