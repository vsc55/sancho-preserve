package sancho.view.irc;

import sancho.utility.VersionInfo;

class IRCShell$6 implements Runnable {
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$6(IRCShell var1, String var2) {
      this.this$0 = var1;
      this.val$message = var2;
   }

   public void run() {
      this.this$0.shell.setText(VersionInfo.getName() + "IRC" + (this.val$message == null ? "" : ": " + this.val$message));
   }
}
