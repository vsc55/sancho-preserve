package sancho.view.console;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;

class Console$2 extends MouseTrackAdapter {
   // $VF: synthetic field
   private final Console this$0;

   Console$2(Console var1) {
      this.this$0 = var1;
   }

   public void mouseExit(MouseEvent var1) {
      this.this$0.disableHand();
   }
}
