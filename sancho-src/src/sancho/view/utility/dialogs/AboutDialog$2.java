package sancho.view.utility.dialogs;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

class AboutDialog$2 implements MouseMoveListener {
   // $VF: synthetic field
   private final AboutDialog this$0;

   AboutDialog$2(AboutDialog var1) {
      this.this$0 = var1;
   }

   public void mouseMove(MouseEvent var1) {
      Canvas var2 = (Canvas)var1.widget;
      if (this.this$0.btRect.contains(var1.x, var1.y) && !this.this$0.mOver) {
         var2.getShell().setCursor(this.this$0.cursorOver);
         GC var5 = new GC(var2);
         this.this$0.drawTextAt(var5, 1, "Graphics:", "Bruce Thomas", 348);
         var5.dispose();
         this.this$0.mOver = true;
      } else if (this.this$0.urlRect.contains(var1.x, var1.y) && !this.this$0.mOver) {
         var2.getShell().setCursor(this.this$0.cursorOver);
         GC var4 = new GC(var2);
         var4.dispose();
         this.this$0.mOver = true;
      } else if (this.this$0.roRect.contains(var1.x, var1.y) && !this.this$0.mOver) {
         var2.getShell().setCursor(this.this$0.cursorOver);
         GC var3 = new GC(var2);
         this.this$0.drawTextAt(var3, 1, "Code:", "Rutger Ovidius", 328);
         var3.dispose();
         this.this$0.mOver = true;
      } else if (this.this$0.mOver
         && !this.this$0.roRect.contains(var1.x, var1.y)
         && !this.this$0.btRect.contains(var1.x, var1.y)
         && !this.this$0.urlRect.contains(var1.x, var1.y)) {
         var2.redraw();
         var2.getShell().setCursor(null);
         this.this$0.mOver = false;
      }
   }
}
