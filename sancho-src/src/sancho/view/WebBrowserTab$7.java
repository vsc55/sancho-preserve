package sancho.view;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import sancho.view.utility.NoDuplicatesCombo;

class WebBrowserTab$7 extends KeyAdapter {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$7(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void keyPressed(KeyEvent var1) {
      NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
      if (var1.character == '\r' || var1.character == 16777296) {
         this.this$0.navigate(var2.getText());
         var2.add(var2.getText(), 0);
         var2.setText("");
      }
   }
}
