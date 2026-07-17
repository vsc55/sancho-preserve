package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

class RootPreferencePage$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final Combo val$c;
   // $VF: synthetic field
   private final RootPreferencePage this$0;

   RootPreferencePage$1(RootPreferencePage var1, Combo var2) {
      this.this$0 = var1;
      this.val$c = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      PreferenceLoader.getPreferenceStore().setValue("locale", this.val$c.getItem(this.val$c.getSelectionIndex()));
   }
}
