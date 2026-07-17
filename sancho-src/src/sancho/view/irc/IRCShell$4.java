package sancho.view.irc;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.console.IRCConsole;

class IRCShell$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$4(IRCShell var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      this.this$0.selectedChannel = var2.getText();
      IRCConsole var3 = this.this$0.getActiveConsole();
      if (var3 != null) {
         var3.setFocus();
      }
   }
}
