package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.irc.IRCConnectDialog;
import sancho.view.irc.IRCShell;
import sancho.view.preferences.PreferenceLoader;

class MenuBar$14 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$14(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if (PreferenceLoader.loadBoolean("ircAutoConnect")) {
         MenuBar.access$100(this.this$0).addIRCShell(new IRCShell());
      } else {
         IRCConnectDialog var2 = new IRCConnectDialog(MenuBar.access$200(this.this$0));
         if (var2.open() == 0) {
            MenuBar.access$100(this.this$0).addIRCShell(new IRCShell());
         }
      }
   }
}
