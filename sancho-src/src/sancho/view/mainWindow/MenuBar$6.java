package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.core.Sancho;

class MenuBar$6 implements Listener {
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$6(MenuBar$1 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      if (Sancho.getCore() != null) {
         Sancho.getCoreFactory().disconnect();
      }
   }
}
