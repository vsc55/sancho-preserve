package sancho.view.search.result;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public final class ResultTableMenuListener$CustomSeparator extends Canvas {
   private int lineWidth;
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$CustomSeparator(ResultTableMenuListener var1, Composite var2) {
      super(var2, 0);
      this.this$0 = var1;
      this.lineWidth = 1;
      this.addPaintListener(new ResultTableMenuListener$10(this));
   }

   public Point computeSize(int var1, int var2, boolean var3) {
      this.checkWidget();
      if (var1 == -1) {
         var1 = this.lineWidth;
      }

      if (var2 == -1) {
         var2 = this.lineWidth;
      }

      return new Point(var1, var2);
   }

   public boolean setFocus() {
      this.checkWidget();
      return false;
   }

   private void onPaint(PaintEvent var1) {
      Rectangle var2 = this.getClientArea();
      if (var2.width != 0 && var2.height != 0) {
         Display var3 = this.getDisplay();
         var1.gc.setLineWidth(this.lineWidth);
         Color var4 = var3.getSystemColor(28);
         var1.gc.setForeground(var4);
         var1.gc.drawLine(0, 0, var2.width - 1, 0);
      }
   }

   // $VF: synthetic method
   static void access$3600(ResultTableMenuListener$CustomSeparator var0, PaintEvent var1) {
      var0.onPaint(var1);
   }
}
