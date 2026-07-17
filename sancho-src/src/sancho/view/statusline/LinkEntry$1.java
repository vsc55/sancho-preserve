package sancho.view.statusline;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

class LinkEntry$1 extends KeyAdapter {
   // $VF: synthetic field
   private final Text val$linkEntryText;
   // $VF: synthetic field
   private final LinkEntry this$0;

   LinkEntry$1(LinkEntry var1, Text var2) {
      this.this$0 = var1;
      this.val$linkEntryText = var2;
   }

   public void keyPressed(KeyEvent var1) {
      if ((var1.stateMask & 262144) != 0 && (var1.character == '\n' || var1.character == '\r' || var1.character == 16777296)) {
         this.this$0.enterLinks(this.val$linkEntryText);
         var1.doit = false;
      }
   }
}
