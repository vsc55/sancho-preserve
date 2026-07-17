package sancho.view.statistics;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class GraphHistory$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final GraphHistory this$0;

   GraphHistory$1(GraphHistory var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      GraphHistory.access$000(this.this$0).redraw();
   }
}
