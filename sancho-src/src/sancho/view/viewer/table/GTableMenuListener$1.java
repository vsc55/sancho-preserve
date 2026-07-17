package sancho.view.viewer.table;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

class GTableMenuListener$1 extends MouseAdapter {
   // $VF: synthetic field
   private final GTableMenuListener this$0;

   GTableMenuListener$1(GTableMenuListener var1) {
      this.this$0 = var1;
   }

   public void mouseDown(MouseEvent var1) {
      if (this.this$0.gView.getItemAt(var1.x, var1.y) == null) {
         this.this$0.deselectAll();
      }
   }
}
