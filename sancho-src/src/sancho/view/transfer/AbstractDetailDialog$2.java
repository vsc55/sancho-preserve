package sancho.view.transfer;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.MainWindow;

class AbstractDetailDialog$2 implements Listener {
   // $VF: synthetic field
   private final CLabel val$cLabel;
   // $VF: synthetic field
   private final AbstractDetailDialog this$0;

   AbstractDetailDialog$2(AbstractDetailDialog var1, CLabel var2) {
      this.this$0 = var1;
      this.val$cLabel = var2;
   }

   public void handleEvent(Event var1) {
      MainWindow.copyToClipboard(this.val$cLabel.getText());
   }
}
