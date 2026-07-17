package sancho.view.utility;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MyViewForm$1 implements Listener {
   // $VF: synthetic field
   private final MyViewForm this$0;

   MyViewForm$1(MyViewForm var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      switch (var1.type) {
         case 9:
            this.this$0.onPaint(var1.gc);
         case 10:
         default:
            break;
         case 11:
            this.this$0.onResize();
            break;
         case 12:
            this.this$0.onDispose();
      }
   }
}
