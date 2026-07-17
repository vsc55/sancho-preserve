package sancho.view.preferences;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

class MLDonkeyPreferencePage$2 extends KeyAdapter {
   // $VF: synthetic field
   private final MLDonkeyPreferencePage this$0;

   MLDonkeyPreferencePage$2(MLDonkeyPreferencePage var1) {
      this.this$0 = var1;
   }

   public void keyReleased(KeyEvent var1) {
      this.this$0.updateRefine(var1);
   }
}
