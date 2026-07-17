package sancho.view.viewer.tree;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class GTreeView$1 implements Listener {
   // $VF: synthetic field
   private final GTreeView this$0;

   GTreeView$1(GTreeView var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      this.this$0.onMove();
   }
}
