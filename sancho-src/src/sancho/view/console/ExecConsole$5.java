package sancho.view.console;

import sancho.utility.MyObservable;

class ExecConsole$5 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final String val$newLine;
   // $VF: synthetic field
   private final ExecConsole this$0;

   ExecConsole$5(ExecConsole var1, MyObservable var2, String var3) {
      this.this$0 = var1;
      this.val$o = var2;
      this.val$newLine = var3;
   }

   public void run() {
      if (!ExecConsole.access$100(this.this$0).isDisposed()) {
         this.this$0.appendLine((ExecConsole$StreamMonitor)this.val$o, this.val$newLine);
      }
   }
}
