package sancho.view.utility;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.utility.VersionInfo;

class VersionChecker$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final VersionChecker$VersionDialog this$0;

   VersionChecker$5(VersionChecker$VersionDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      WebLauncher.openLink(VersionInfo.getHomePage2());
      this.this$0.close();
   }
}
