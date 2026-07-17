package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

class BandwidthDialog$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final BandwidthDialog this$0;

   BandwidthDialog$2(BandwidthDialog var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Combo var2 = (Combo)var1.widget;
      this.this$0.savePreset(this.this$0.oldSelection, var2.getItem(this.this$0.oldSelection));
      this.this$0.selectedPreset = var2.getSelectionIndex();
      this.this$0.oldSelection = this.this$0.selectedPreset;
      this.this$0.loadPreset(this.this$0.selectedPreset, false);
   }
}
