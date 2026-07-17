package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class VersionChecker$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final VersionChecker$VersionDialog this$0;

   VersionChecker$4(VersionChecker$VersionDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.close();
   }
}
