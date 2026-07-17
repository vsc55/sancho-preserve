package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.utility.VersionChecker;

class MenuBar$23 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$23(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      new VersionChecker(MenuBar.access$200(this.this$0), MenuBar.access$100(this.this$0).getStatusline(), 0);
   }
}
