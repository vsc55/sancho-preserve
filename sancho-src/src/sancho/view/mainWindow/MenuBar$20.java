package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MenuBar$20 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$20(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      MenuBar.access$502(this.this$0, true);
      MenuBar.access$200(this.this$0).getDisplay().timerExec(250, this.this$0);
   }
}
