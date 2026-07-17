package sancho.view.search;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Combo;
import sancho.view.preferences.PreferenceLoader;

class ASearchTab$3 implements DisposeListener {
   // $VF: synthetic field
   private final String val$saveString;
   // $VF: synthetic field
   private final ASearchTab this$0;

   ASearchTab$3(ASearchTab var1, String var2) {
      this.this$0 = var1;
      this.val$saveString = var2;
   }

   public void widgetDisposed(DisposeEvent var1) {
      Combo var2 = (Combo)var1.widget;
      if (PreferenceLoader.loadBoolean("searchSaveEntries")) {
         PreferenceLoader.setValue(this.val$saveString + ".sArray", var2.getItems(), 25);
      } else {
         PreferenceLoader.getPreferenceStore().setValue(this.val$saveString + ".sArray", "");
      }
   }
}
