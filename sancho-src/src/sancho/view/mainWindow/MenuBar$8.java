package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.core.Sancho;

class MenuBar$8 implements Listener {
   // $VF: synthetic field
   private final int val$j;
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$8(MenuBar$1 var1, int var2) {
      this.this$1 = var1;
      this.val$j = var2;
   }

   public void handleEvent(Event var1) {
      Sancho.getCoreFactory().reconnect(this.val$j);
   }
}
