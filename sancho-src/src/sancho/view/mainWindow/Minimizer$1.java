package sancho.view.mainWindow;

import sancho.model.mldonkey.ClientStats;

class Minimizer$1 implements Runnable {
   // $VF: synthetic field
   private final ClientStats val$clientStats;
   // $VF: synthetic field
   private final Minimizer this$0;

   Minimizer$1(Minimizer var1, ClientStats var2) {
      this.this$0 = var1;
      this.val$clientStats = var2;
   }

   public void run() {
      if (this.this$0.shell != null && !this.this$0.shell.isDisposed() && this.this$0.shell.getMinimized()) {
         this.this$0.setTitleBar(this.val$clientStats);
      }
   }
}
