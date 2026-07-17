package sancho.view.viewFrame;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class ViewFrame$6 extends KeyAdapter {
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$6(ViewFrame var1) {
      this.this$0 = var1;
   }

   public void keyReleased(KeyEvent var1) {
      this.this$0.updateRefine(var1);
   }
}
