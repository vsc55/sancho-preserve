package sancho.view.downloadComplete;

import java.io.File;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import sancho.utility.VersionInfo;

class DownloadCompleteShell$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final Button val$b;
   // $VF: synthetic field
   private final DownloadCompleteShell this$0;

   DownloadCompleteShell$1(DownloadCompleteShell var1, Button var2) {
      this.this$0 = var1;
      this.val$b = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      File var2 = new File(VersionInfo.getDownloadLogFile());
      var2.delete();
      this.val$b.setEnabled(false);
   }
}
