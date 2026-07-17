package sancho.view.search.result;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

class ResultTableMenuListener$ToolTipTimerTask implements Runnable {
   ResultTableMenuListener$ToolTipHandler toolTipHandler;
   boolean stop;
   Display display;
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$ToolTipTimerTask(ResultTableMenuListener var1, ResultTableMenuListener$ToolTipHandler var2) {
      this.this$0 = var1;
      this.toolTipHandler = var2;
      this.display = ResultTableMenuListener$ToolTipHandler.access$3000(var2);
   }

   public void run() {
      if (!this.stop && this.display != null && !this.display.isDisposed()) {
         this.display.timerExec(1000, this);
      }

      if (ResultTableMenuListener$ToolTipHandler.access$3100(this.toolTipHandler)) {
         Point var1 = ResultTableMenuListener$ToolTipHandler.access$3000(this.toolTipHandler).getCursorLocation();
         if (ResultTableMenuListener.access$3200(this.this$0) != null
            && !ResultTableMenuListener.access$3300(this.this$0).isDisposed()
            && ResultTableMenuListener.access$3400(this.this$0).getComposite().getVerticalBar() != null) {
            int var2 = ResultTableMenuListener.access$3500(this.this$0).getComposite().getVerticalBar().getSelection();
            if (var1.equals(ResultTableMenuListener$ToolTipHandler.access$2000(this.toolTipHandler)) && var2 == this.toolTipHandler.vscroll) {
               return;
            }
         }

         Shell var3 = ResultTableMenuListener$ToolTipHandler.access$900(this.toolTipHandler);
         if (var3 != null && !var3.isDisposed() && var3.isVisible() && !var3.getBounds().contains(var1)) {
            ResultTableMenuListener$ToolTipHandler.access$1700(this.toolTipHandler, false);
         }
      }
   }
}
