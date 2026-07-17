package sancho.view.utility;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class LinkRipper$7 extends KeyAdapter {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$7(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.character == '\r' || var1.character == 16777296) {
         this.this$0.ripLinks();
      }
   }
}
