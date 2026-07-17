package sancho.view.mainWindow;

import sancho.model.mldonkey.ClientStats;

class MinimizerTray$3 implements Runnable {
   // $VF: synthetic field
   private final ClientStats val$clientStats;
   // $VF: synthetic field
   private final MinimizerTray this$0;

   MinimizerTray$3(MinimizerTray var1, ClientStats var2) {
      this.this$0 = var1;
      this.val$clientStats = var2;
   }

   public void run() {
      if (this.this$0.shell != null
         && !this.this$0.shell.isDisposed()
         && MinimizerTray.access$100(this.this$0) != null
         && !MinimizerTray.access$100(this.this$0).isDisposed()) {
         if (this.this$0.shell.isVisible() && this.this$0.shell.getMinimized()) {
            this.this$0.setTitleBar(this.val$clientStats);
         }

         this.this$0.setTrayToolTip(this.val$clientStats);
      }
   }
}
