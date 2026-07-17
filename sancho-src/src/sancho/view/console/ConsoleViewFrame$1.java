package sancho.view.console;

import java.util.StringTokenizer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;
import sancho.view.preferences.PreferenceLoader;

class ConsoleViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final int val$num;
   // $VF: synthetic field
   private final ConsoleViewFrame this$0;

   ConsoleViewFrame$1(ConsoleViewFrame var1, int var2) {
      this.this$0 = var1;
      this.val$num = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      String var2 = PreferenceLoader.loadString("consoleToolItem" + this.val$num);
      if (!var2.equals("")) {
         if (var2.indexOf(";") != -1) {
            StringTokenizer var3 = new StringTokenizer(var2, ";");

            while (var3.hasMoreTokens()) {
               Sancho.send((short)29, var3.nextToken());
            }
         } else {
            Sancho.send((short)29, var2);
         }
      }
   }
}
