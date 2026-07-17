package sancho.view.irc;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;

class IRCShell$1 implements DisposeListener {
   // $VF: synthetic field
   private final IRCShell this$0;

   IRCShell$1(IRCShell var1) {
      this.this$0 = var1;
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(var2, "ircWindowBounds", this.this$0.shell.getBounds());
   }
}
