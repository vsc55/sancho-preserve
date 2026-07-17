package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$18 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$18(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      new MenuBar$AlphaInputDialog(MenuBar.access$200(this.this$0)).open();
   }
}
