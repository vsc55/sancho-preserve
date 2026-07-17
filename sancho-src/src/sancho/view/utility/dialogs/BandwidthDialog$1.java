package sancho.view.utility.dialogs;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Combo;

class BandwidthDialog$1 implements VerifyListener {
   // $VF: synthetic field
   private final BandwidthDialog this$0;

   BandwidthDialog$1(BandwidthDialog var1) {
      this.this$0 = var1;
   }

   public void verifyText(VerifyEvent var1) {
      if (!this.this$0.modifying) {
         this.this$0.modifying = true;
         Combo var2 = (Combo)var1.widget;
         String var3 = var2.getText() + var1.character;
         if (!var3.equals("\u0000")) {
            var2.setItem(this.this$0.selectedPreset, var3);
         }

         this.this$0.modifying = false;
      }
   }
}
