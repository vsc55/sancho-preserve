package sancho.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class SearchTab$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final SearchTab this$0;

   SearchTab$4(SearchTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      SearchTab.access$200(this.this$0).performSearch();
   }
}
