package sancho.view.console;

class IRCConsole$1 implements Runnable {
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final IRCConsole this$0;

   IRCConsole$1(IRCConsole var1, String var2) {
      this.this$0 = var1;
      this.val$message = var2;
   }

   public void run() {
      this.this$0.appendNewLine(this.val$message);
   }
}
