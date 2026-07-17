package sancho.view.transfer;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class FileDetailDialog$6 extends KeyAdapter {
   // $VF: synthetic field
   private final FileDetailDialog this$0;

   FileDetailDialog$6(FileDetailDialog var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.character == '\r') {
         FileDetailDialog.access$200(this.this$0);
         FileDetailDialog.access$100(this.this$0).setText("");
      }
   }
}
