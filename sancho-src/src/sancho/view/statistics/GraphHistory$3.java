package sancho.view.statistics;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

class GraphHistory$3 extends ControlAdapter {
   // $VF: synthetic field
   private final GraphHistory this$0;

   GraphHistory$3(GraphHistory var1) {
      this.this$0 = var1;
   }

   public void controlResized(ControlEvent var1) {
      this.this$0.adjustScrollBar();
   }
}
