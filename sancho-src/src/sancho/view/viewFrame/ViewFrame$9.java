package sancho.view.viewFrame;

class ViewFrame$9 implements Runnable {
   // $VF: synthetic field
   private final String val$string;
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$9(ViewFrame var1, String var2) {
      this.this$0 = var1;
      this.val$string = var2;
   }

   public void run() {
      this.this$0.updateCLabelToolTip(this.val$string);
   }
}
