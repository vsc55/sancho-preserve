package sancho.view.statusline;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class CoreConsoleItem$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final CoreConsoleItem this$0;

   CoreConsoleItem$1(CoreConsoleItem var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Sancho.getCoreConsole().getShell().open();
   }
}
