package sancho.view.console;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;

class ExecConsole$1 implements DisposeListener {
   // $VF: synthetic field
   private final ExecConsole this$0;

   ExecConsole$1(ExecConsole var1) {
      this.this$0 = var1;
   }

   public synchronized void widgetDisposed(DisposeEvent var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      PreferenceConverter.setValue(var2, "coreExecutableWindowBounds", ExecConsole.access$000(this.this$0).getBounds());
   }
}
