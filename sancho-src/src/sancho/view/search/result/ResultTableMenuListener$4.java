package sancho.view.search.result;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

class ResultTableMenuListener$4 implements DisposeListener {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$4(ResultTableMenuListener$ToolTipHandler var1) {
      this.this$1 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      if (ResultTableMenuListener$ToolTipHandler.access$1000(this.this$1) != null) {
         ResultTableMenuListener$ToolTipHandler.access$1000(this.this$1).dispose();
      }

      if (ResultTableMenuListener$ToolTipHandler.access$1100(this.this$1) != null) {
         ResultTableMenuListener$ToolTipHandler.access$1100(this.this$1).dispose();
      }
   }
}
