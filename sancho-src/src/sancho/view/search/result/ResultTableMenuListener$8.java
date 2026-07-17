package sancho.view.search.result;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

class ResultTableMenuListener$8 extends MouseAdapter {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$8(ResultTableMenuListener$ToolTipHandler var1) {
      this.this$1 = var1;
   }

   public void mouseDown(MouseEvent var1) {
      if (ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).isVisible()) {
         ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
      }
   }
}
