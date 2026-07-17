package sancho.view.console;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class ExecConsole$2 implements Listener {
   // $VF: synthetic field
   private final ExecConsole this$0;

   ExecConsole$2(ExecConsole var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      var1.doit = false;
      ExecConsole.access$000(this.this$0).setVisible(false);
   }
}
