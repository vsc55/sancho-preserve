package sancho.view.irc;

import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;

class IRCShell$3 extends CTabFolder2Adapter {
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$3(IRCShell var1) {
      this.this$0 = var1;
   }

   public void close(CTabFolderEvent var1) {
      if (this.this$0.cTabFolder.getItemCount() > 1) {
         synchronized (this) {
            CTabItem var3 = (CTabItem)var1.item;
            if (var3.getText().equals("status")) {
               var1.doit = false;
               return;
            }

            this.this$0.ircClient.partChannel(var3.getText());
            IRCShell$IRCTab var4 = (IRCShell$IRCTab)this.this$0.tabMap.get(var3.getText());
            var4.dispose();
            this.this$0.tabMap.remove(var3.getText());
         }
      } else {
         var1.doit = false;
      }
   }
}
