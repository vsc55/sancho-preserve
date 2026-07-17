package sancho.view.utility.setupWizard;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class CoreBinaryPage$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final CoreBinaryPage this$0;

   CoreBinaryPage$1(CoreBinaryPage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      FileDialog var2 = new FileDialog(this.this$0.getShell(), 0);
      var2.setFilterExtensions(new String[]{"*"});
      String var3;
      if ((var3 = var2.open()) != null) {
         this.this$0.text.setText(var3);
      }
   }
}
