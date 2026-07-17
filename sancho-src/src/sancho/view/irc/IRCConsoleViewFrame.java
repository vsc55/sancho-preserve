package sancho.view.irc;

import org.eclipse.swt.custom.SashForm;
import sancho.view.console.IRCConsole;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class IRCConsoleViewFrame extends SashViewFrame {
   protected IRCConsole ircConsole;

   public IRCConsoleViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, String var5) {
      super(var1, var2, var3, var4);
      this.ircConsole = new IRCConsole(this.getChildComposite(), 64, var5);
      this.createViewListener(new IRCConsoleViewListener(this));
   }

   public IRCConsole getConsole() {
      return this.ircConsole;
   }
}
