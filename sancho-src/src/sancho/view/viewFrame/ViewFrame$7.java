package sancho.view.viewFrame;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

class ViewFrame$7 implements DisposeListener {
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$7(ViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      this.this$0.menuManager.dispose();
   }
}
