package sancho.view.search.result;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

class ResultTableMenuListener$3 implements PaintListener {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$3(ResultTableMenuListener$ToolTipHandler var1) {
      this.this$1 = var1;
   }

   public void paintControl(PaintEvent var1) {
      if (ResultTableMenuListener$ToolTipHandler.access$800(this.this$1) != null) {
         var1.gc.setBackground(ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).getDisplay().getSystemColor(29));
         var1.gc.setForeground(ResultTableMenuListener$ToolTipHandler.access$900(this.this$1).getDisplay().getSystemColor(28));
         var1.gc.fillPolygon(ResultTableMenuListener$ToolTipHandler.access$800(this.this$1));
         var1.gc.drawPolygon(ResultTableMenuListener$ToolTipHandler.access$800(this.this$1));
      }
   }
}
