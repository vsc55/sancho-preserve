package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class FileDetailDialog$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$5(FileDetailDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      String var2 = FileDetailDialog.access$000(this.this$0).getSelection()[0];
      FileDetailDialog.access$100(this.this$0).setText(var2);
   }
}
