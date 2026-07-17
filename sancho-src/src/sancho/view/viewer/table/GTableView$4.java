package sancho.view.viewer.table;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class GTableView$4 implements Listener {
   // $VF: synthetic field
   private final int val$columnIndex;
   // $VF: synthetic field
   private final GTableView this$0;

   GTableView$4(GTableView var1, int var2) {
      this.this$0 = var1;
      this.val$columnIndex = var2;
   }

   public void handleEvent(Event var1) {
      this.this$0.sortByColumn(this.val$columnIndex);
      this.this$0.setSortIndicator();
   }
}
