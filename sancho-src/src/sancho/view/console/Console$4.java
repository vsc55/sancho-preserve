package sancho.view.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class Console$4 extends KeyAdapter {
   // $VF: synthetic field
   private final Console this$0;

   Console$4(Console var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.stateMask == SWT.MOD1 && var1.character == 6) {
         new Console$FindDialog(this.this$0).open();
      }
   }
}
