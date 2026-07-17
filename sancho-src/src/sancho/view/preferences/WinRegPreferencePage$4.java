package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class WinRegPreferencePage$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final int val$type;
   // $VF: synthetic field
   private final WinRegPreferencePage$RegisterExtension this$0;

   WinRegPreferencePage$4(WinRegPreferencePage$RegisterExtension var1, int var2) {
      this.this$0 = var1;
      this.val$type = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      WinRegPreferencePage$RegisterExtension.access$402(this.this$0, this.val$type);
   }
}
