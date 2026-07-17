package sancho.view.irc;

import java.util.ArrayList;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.WidgetFactory;

class IRCShell$5 implements Runnable {
   // $VF: synthetic field
   private final String val$channelName;
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$5(IRCShell var1, String var2) {
      this.this$0 = var1;
      this.val$channelName = var2;
   }

   public void run() {
      CTabItem var1 = new CTabItem(this.this$0.cTabFolder, 0);
      var1.setText(this.val$channelName);
      IRCShell$IRCTab var2 = new IRCShell$IRCTab();
      this.this$0.selectedChannel = this.val$channelName;
      this.this$0.tabMap.put(this.val$channelName.toLowerCase(), var2);
      String var3 = "ircStatusSash";
      if (!this.val$channelName.equals("status")) {
         var3 = "ircSash";
      }

      SashForm var4 = WidgetFactory.createSashForm(this.this$0.cTabFolder, var3);
      var2.setConsoleFrame(this.this$0.createConsole(var4, this.val$channelName));
      if (!this.val$channelName.equals("status")) {
         var2.setView(this.this$0.createUserTable(var4));
         var2.setCUsers(new ArrayList());
      }

      WidgetFactory.loadSashForm(var4, var3);
      var1.setControl(var4);
      this.this$0.cTabFolder.setSelection(var1);
      var2.getConsole().setFocus();
   }
}
