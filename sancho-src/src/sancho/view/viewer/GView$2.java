package sancho.view.viewer;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

class GView$2 extends ControlAdapter {
   // $VF: synthetic field
   private final GView this$0;

   GView$2(GView var1) {
      this.this$0 = var1;
   }

   public void controlResized(ControlEvent var1) {
      this.this$0.resizeColumns();
   }
}
