package sancho.view.viewer.tree;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class GTreeView$4 implements Listener {
   // $VF: synthetic field
   private final int val$columnIndex;
   // $VF: synthetic field
   private final GTreeView this$0;

   GTreeView$4(GTreeView var1, int var2) {
      this.this$0 = var1;
      this.val$columnIndex = var2;
   }

   public void handleEvent(Event var1) {
      this.this$0.sortByColumn(this.val$columnIndex);
      this.this$0.setSortIndicator();
   }
}
