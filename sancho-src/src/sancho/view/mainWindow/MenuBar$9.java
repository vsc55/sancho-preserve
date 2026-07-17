package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$9 implements Listener {
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$9(MenuBar$1 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      MenuBar.access$100(MenuBar$1.access$000(this.this$1)).getMinimizer().forceClose();
      MenuBar.access$200(MenuBar$1.access$000(this.this$1)).close();
   }
}
