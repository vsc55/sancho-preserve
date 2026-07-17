package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.utility.VersionInfo;
import sancho.view.utility.WebLauncher;

class BugDialog$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final BugDialog this$0;

   BugDialog$1(BugDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      WebLauncher.openLink("mailto:" + VersionInfo.getEmail());
   }
}
