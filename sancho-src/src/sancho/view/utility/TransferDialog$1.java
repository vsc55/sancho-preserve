package sancho.view.utility;

import org.eclipse.swt.custom.CLabel;

class TransferDialog$1 implements Runnable {
   // $VF: synthetic field
   private final CLabel val$clabel;
   // $VF: synthetic field
   private final String val$text;
   // $VF: synthetic field
   private final TransferDialog this$0;

   TransferDialog$1(TransferDialog var1, CLabel var2, String var3) {
      this.this$0 = var1;
      this.val$clabel = var2;
      this.val$text = var3;
   }

   public void run() {
      if (this.val$clabel != null && !this.val$clabel.isDisposed()) {
         this.val$clabel.setText(this.val$text);
      }
   }
}
