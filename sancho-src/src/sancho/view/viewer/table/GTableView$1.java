package sancho.view.viewer.table;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class GTableView$1 implements Listener {
   // $VF: synthetic field
   private final GTableView this$0;

   GTableView$1(GTableView var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      this.this$0.onMove();
   }
}
