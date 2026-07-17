package sancho.view.viewer.actions;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

class TransferAction$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final TransferAction$PreviewDownloadDialog this$1;

   TransferAction$1(TransferAction$PreviewDownloadDialog var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Shell var2 = this.this$1.getShell();
      DirectoryDialog var3 = new DirectoryDialog(var2, 0);
      String var4;
      if ((var4 = var3.open()) != null) {
         this.this$1.text.setText(var4);
      }
   }
}
