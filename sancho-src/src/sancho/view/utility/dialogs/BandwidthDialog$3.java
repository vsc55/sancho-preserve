package sancho.view.utility.dialogs;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.model.mldonkey.Option;
import sancho.view.utility.BSpinner;

class BandwidthDialog$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final BSpinner val$spinner;
   // $VF: synthetic field
   private final Option val$option;
   // $VF: synthetic field
   private final BandwidthDialog this$0;

   BandwidthDialog$3(BandwidthDialog var1, BSpinner var2, Option var3) {
      this.this$0 = var1;
      this.val$spinner = var2;
      this.val$option = var3;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.val$spinner.setSelection(Integer.parseInt(this.val$option.getDefaultValue()));
   }
}
