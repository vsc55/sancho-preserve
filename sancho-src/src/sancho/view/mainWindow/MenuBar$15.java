package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.utility.dialogs.DebugDialog;

class MenuBar$15 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$15(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      new DebugDialog(MenuBar.access$200(this.this$0)).open();
   }
}
