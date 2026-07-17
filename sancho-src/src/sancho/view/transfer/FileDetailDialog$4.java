package sancho.view.transfer;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

class FileDetailDialog$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final Text val$mirrorText;
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$4(FileDetailDialog var1, Text var2) {
      this.this$0 = var1;
      this.val$mirrorText = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.addMirror(this.val$mirrorText.getText());
      this.val$mirrorText.setText("");
   }
}
