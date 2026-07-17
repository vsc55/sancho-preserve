package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$22 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$22(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      MenuBar.access$100(this.this$0).openPreferences();
   }
}
