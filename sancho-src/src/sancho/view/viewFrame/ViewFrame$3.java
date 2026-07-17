package sancho.view.viewFrame;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.NoDuplicatesCombo;

class ViewFrame$3 implements DisposeListener {
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$3(ViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetDisposed(DisposeEvent var1) {
      NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
      String[] var3 = var2.getItems();

      for (int var4 = 0; var4 < var3.length; var4++) {
         if (var3[var4].length() > 1000) {
            var3[var4] = var3[var4].substring(0, 1000);
         }
      }

      PreferenceLoader.setValue(this.this$0.prefString + ".refineSArray", var3, 25);
   }
}
