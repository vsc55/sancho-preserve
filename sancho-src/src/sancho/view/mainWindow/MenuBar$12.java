package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$12 implements Listener {
   // $VF: synthetic field
   private final MenuBar$10 this$1;

   MenuBar$12(MenuBar$10 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      MenuBar.access$100(MenuBar$10.access$400(this.this$1)).configureTabs();
   }
}
