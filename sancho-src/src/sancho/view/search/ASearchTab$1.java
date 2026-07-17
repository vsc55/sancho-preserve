package sancho.view.search;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class ASearchTab$1 extends KeyAdapter {
   // $VF: synthetic field
   private final ASearchTab this$0;

   ASearchTab$1(ASearchTab var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      switch (var1.keyCode) {
         case 8:
         case 127:
         case 16777219:
         case 16777220:
            return;
         default:
            try {
               Integer.parseInt(String.valueOf(var1.character));
            } catch (NumberFormatException var3) {
               var1.doit = false;
            }
      }
   }
}
