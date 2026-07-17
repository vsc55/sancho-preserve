package sancho.view.utility;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class IDSelector$2 implements Listener {
   // $VF: synthetic field
   private final IDSelector this$0;

   IDSelector$2(IDSelector var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      switch (var1.type) {
         case 3:
            this.this$0.onMouseDown(var1);
            break;
         case 4:
            this.this$0.onMouseUp(var1);
            break;
         case 5:
            this.this$0.onMouseMove(var1);
      }
   }
}
