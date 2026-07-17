package sancho.view.statusline;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import sancho.core.Sancho;

class RateItem$1 extends MouseAdapter {
   // $VF: synthetic field
   private final RateItem this$0;

   RateItem$1(RateItem var1) {
      this.this$0 = var1;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      Sancho.getCoreFactory().reconnect();
   }
}
