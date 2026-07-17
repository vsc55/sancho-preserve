package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.core.Sancho;

class MenuBar$7 implements Listener {
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$7(MenuBar$1 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      Sancho.getCoreFactory().reconnect();
   }
}
