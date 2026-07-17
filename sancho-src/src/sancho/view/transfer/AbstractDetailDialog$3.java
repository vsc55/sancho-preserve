package sancho.view.transfer;

class AbstractDetailDialog$3 implements Runnable {
   // $VF: synthetic field
   private final int val$flag;
   // $VF: synthetic field
   private final AbstractDetailDialog this$0;

   AbstractDetailDialog$3(AbstractDetailDialog var1, int var2) {
      this.this$0 = var1;
      this.val$flag = var2;
   }

   public void run() {
      if (this.this$0.getShell() != null && !this.this$0.getShell().isDisposed()) {
         this.this$0.updateLabels();
         this.this$0.updateViews(this.val$flag);
      }
   }
}
