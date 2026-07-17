package sancho.view.console;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.MainWindow;

class ExecConsole$3 implements Listener {
   // $VF: synthetic field
   private final ExecConsole this$0;

   ExecConsole$3(ExecConsole var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      MainWindow.copyToClipboard(ExecConsole.access$100(this.this$0).getSelectionText());
   }
}
