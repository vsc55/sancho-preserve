package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class ClientFilesDialog$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final ClientFilesDialog this$0;

   ClientFilesDialog$2(ClientFilesDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.close();
   }
}
