package sancho.view.shares;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;

class UploadViewFrame$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final UploadViewFrame$ShareDialog this$1;

   UploadViewFrame$4(UploadViewFrame$ShareDialog var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Button var2 = (Button)var1.widget;
      DirectoryDialog var3 = new DirectoryDialog(var2.getShell(), 0);
      String var4;
      if ((var4 = var3.open()) != null) {
         UploadViewFrame$ShareDialog.access$200(this.this$1).setText(var4);
      }
   }
}
