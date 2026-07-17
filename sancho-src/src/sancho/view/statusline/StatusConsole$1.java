package sancho.view.statusline;

import sancho.core.Sancho;

class StatusConsole$1 implements Runnable {
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final StatusConsole this$0;

   StatusConsole$1(StatusConsole var1, String var2) {
      this.this$0 = var1;
      this.val$message = var2;
   }

   public void run() {
      if (Sancho.hasCollectionFactory() && StatusConsole.access$000(this.this$0) != null && !StatusConsole.access$000(this.this$0).isDisposed()) {
         StatusConsole.access$000(this.this$0).append(this.val$message);
      }
   }
}
