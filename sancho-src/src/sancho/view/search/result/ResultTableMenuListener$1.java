package sancho.view.search.result;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

class ResultTableMenuListener$1 implements DisposeListener {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler val$toolTipHandler;
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   ResultTableMenuListener$1(ResultTableMenuListener var1, ResultTableMenuListener$ToolTipHandler var2) {
      this.this$0 = var1;
      this.val$toolTipHandler = var2;
   }

   public void widgetDisposed(DisposeEvent var1) {
      this.val$toolTipHandler.dispose();
   }
}
