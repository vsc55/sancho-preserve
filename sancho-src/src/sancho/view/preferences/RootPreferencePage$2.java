package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

class RootPreferencePage$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final RootPreferencePage this$0;

   RootPreferencePage$2(RootPreferencePage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Combo var2 = (Combo)var1.widget;
      PreferenceLoader.getPreferenceStore().setValue("dlFileDoubleClick", var2.getSelectionIndex());
   }
}
