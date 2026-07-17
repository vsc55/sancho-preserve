package sancho.view.statistics;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

class GraphCanvas$2 extends MouseAdapter {
   // $VF: synthetic field
   private final GraphCanvas this$0;

   GraphCanvas$2(GraphCanvas var1) {
      this.this$0 = var1;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      this.this$0.toggleDisplay();
   }
}
