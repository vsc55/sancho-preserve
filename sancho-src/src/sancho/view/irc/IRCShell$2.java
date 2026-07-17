package sancho.view.irc;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class IRCShell$2 implements Listener {
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$2(IRCShell var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      this.this$0.close();
   }
}
