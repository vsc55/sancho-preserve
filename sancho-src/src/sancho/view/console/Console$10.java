package sancho.view.console;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class Console$10 extends KeyAdapter {
   // $VF: synthetic field
   private final Console$FindDialog this$1;

   Console$10(Console$FindDialog var1) {
      this.this$1 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      switch (var1.keyCode) {
         case 13:
         case 16777296:
            this.this$1.find();
      }
   }
}
