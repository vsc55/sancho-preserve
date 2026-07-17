package sancho.view.utility;

import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import sancho.view.preferences.PreferenceLoader;

class WidgetFactory$1 implements DisposeListener {
   // $VF: synthetic field
   private final String val$orientationPrefString;
   // $VF: synthetic field
   private final SashForm val$sashForm;

   WidgetFactory$1(String var1, SashForm var2) {
      this.val$orientationPrefString = var1;
      this.val$sashForm = var2;
   }

   public void widgetDisposed(DisposeEvent var1) {
      PreferenceStore var2 = PreferenceLoader.getPreferenceStore();
      var2.setValue(this.val$orientationPrefString, this.val$sashForm.getOrientation());
   }
}
