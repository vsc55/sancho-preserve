package sancho.view.irc;

import org.jibble.pircbot.NickAlreadyInUseException;

class IRCClient$1 extends Thread {
   // $VF: synthetic field
   private final IRCClient this$0;

   IRCClient$1(IRCClient var1) {
      this.this$0 = var1;
   }

   public void run() {
      try {
         while (true) {
            if (this.this$0.nickCounter < 7 && !this.this$0.connected) {
               try {
                  this.this$0.connect(this.this$0.server);
                  this.this$0.connected = true;
               } catch (NickAlreadyInUseException var2) {
                  IRCClient.access$000(this.this$0, this.this$0.nickname + "_" + this.this$0.nickCounter++);
               }
            } else {
               this.this$0.joinChannel(this.this$0.channel);
               break;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
}
