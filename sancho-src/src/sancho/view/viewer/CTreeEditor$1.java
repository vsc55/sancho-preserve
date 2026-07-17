package sancho.view.viewer;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;

class CTreeEditor$1 implements ControlListener {
   // $VF: synthetic field
   private final CTreeEditor this$0;

   CTreeEditor$1(CTreeEditor var1) {
      this.this$0 = var1;
   }

   public void controlMoved(ControlEvent var1) {
      this.this$0.resize();
   }

   public void controlResized(ControlEvent var1) {
      this.this$0.resize();
   }
}
