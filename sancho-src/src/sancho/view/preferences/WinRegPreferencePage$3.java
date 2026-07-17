package sancho.view.preferences;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class WinRegPreferencePage$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final int val$type;
   // $VF: synthetic field
   private final WinRegPreferencePage$RegisterLink this$0;

   WinRegPreferencePage$3(WinRegPreferencePage$RegisterLink var1, int var2) {
      this.this$0 = var1;
      this.val$type = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      WinRegPreferencePage$RegisterLink.access$302(this.this$0, this.val$type);
   }
}
