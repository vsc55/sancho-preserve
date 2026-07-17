package sancho.view.transfer;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

class FileDetailDialog$3 extends KeyAdapter {
   // $VF: synthetic field
   private final Text val$mirrorText;
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$3(FileDetailDialog var1, Text var2) {
      this.this$0 = var1;
      this.val$mirrorText = var2;
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.character == '\r') {
         this.this$0.addMirror(this.val$mirrorText.getText());
         this.val$mirrorText.setText("");
      }
   }
}
