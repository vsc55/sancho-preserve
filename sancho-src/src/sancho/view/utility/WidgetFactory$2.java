package sancho.view.utility;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;

class WidgetFactory$2 implements DisposeListener {
   // $VF: synthetic field
   private final PreferenceStore val$p;
   // $VF: synthetic field
   private final String val$sashChildPrefString;
   // $VF: synthetic field
   private final int val$childNumber;

   WidgetFactory$2(PreferenceStore var1, String var2, int var3) {
      this.val$p = var1;
      this.val$sashChildPrefString = var2;
      this.val$childNumber = var3;
   }

   public void widgetDisposed(DisposeEvent var1) {
      Control var2 = (Control)var1.widget;
      if (var2.getBounds().width > 10 && var2.getBounds().height > 10) {
         PreferenceConverter.setValue(this.val$p, this.val$sashChildPrefString + this.val$childNumber, var2.getBounds());
      }
   }
}
