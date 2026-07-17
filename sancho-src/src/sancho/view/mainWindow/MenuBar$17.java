package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$17 implements Listener {
   // $VF: synthetic field
   private final int val$ii;
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$17(MenuBar var1, int var2) {
      this.this$0 = var1;
      this.val$ii = var2;
   }

   public void handleEvent(Event var1) {
      int var2 = 255 - 255 * this.val$ii * 10 / 100;
      MenuBar.access$200(this.this$0).setAlpha(var2);
   }
}
