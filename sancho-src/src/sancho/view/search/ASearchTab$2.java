package sancho.view.search;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import sancho.view.utility.NoDuplicatesCombo;

class ASearchTab$2 extends KeyAdapter {
   // $VF: synthetic field
   private final ASearchTab this$0;

   ASearchTab$2(ASearchTab var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      if (var1.character == '\r' || var1.character == 16777296) {
         this.this$0.performSearch();
         NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
         var2.add(var2.getText(), 0);
         var2.setText("");
      }
   }
}
