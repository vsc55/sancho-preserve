package sancho.view;

import sancho.core.Sancho;

class ConsoleTab$1 implements Runnable {
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final ConsoleTab this$0;

   ConsoleTab$1(ConsoleTab var1, String var2) {
      this.this$0 = var1;
      this.val$message = var2;
   }

   public void run() {
      if (Sancho.hasCollectionFactory() && ConsoleTab.access$000(this.this$0) != null && !ConsoleTab.access$000(this.this$0).isDisposed()) {
         ConsoleTab.access$000(this.this$0).append(this.val$message);
      }
   }
}
