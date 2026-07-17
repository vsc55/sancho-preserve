package sancho.view.console;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

class Console$1 implements MouseMoveListener {
   // $VF: synthetic field
   private final Console this$0;

   Console$1(Console var1) {
      this.this$0 = var1;
   }

   public void mouseMove(MouseEvent var1) {
      StyledText var2 = (StyledText)var1.widget;

      try {
         int var3 = var2.getOffsetAtLocation(new Point(var1.x, var1.y));
         StyleRange var4 = var2.getStyleRangeAtOffset(var3);
         if (var4 == null || !var4.underline) {
            this.this$0.disableHand();
         } else if (!this.this$0.usingHand) {
            var2.setCursor(this.this$0.handCursor);
            this.this$0.usingHand = true;
         }
      } catch (IllegalArgumentException var5) {
         this.this$0.disableHand();
      }
   }
}
