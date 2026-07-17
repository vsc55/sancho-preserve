package sancho.view.console;

class ExecConsole$4 extends Thread {
   // $VF: synthetic field
   private final ExecConsole this$0;

   ExecConsole$4(ExecConsole var1) {
      this.this$0 = var1;
   }

   public void run() {
      this.this$0.forceKill();
   }
}
