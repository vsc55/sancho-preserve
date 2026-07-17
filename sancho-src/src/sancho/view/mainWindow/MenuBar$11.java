package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.utility.AbstractTab;

class MenuBar$11 implements Listener {
   // $VF: synthetic field
   private final AbstractTab val$aTab;
   // $VF: synthetic field
   private final MenuBar$10 this$1;

   MenuBar$11(MenuBar$10 var1, AbstractTab var2) {
      this.this$1 = var1;
      this.val$aTab = var2;
   }

   public void handleEvent(Event var1) {
      this.val$aTab.setActive();
   }
}
