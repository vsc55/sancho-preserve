package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.core.Sancho;
import sancho.view.utility.dialogs.CoreVerbosityDialog;

class MenuBar$16 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$16(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if (Sancho.hasCollectionFactory()) {
         new CoreVerbosityDialog(MenuBar.access$200(this.this$0)).open();
      }
   }
}
