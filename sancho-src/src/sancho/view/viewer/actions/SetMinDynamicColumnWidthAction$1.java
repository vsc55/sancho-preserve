package sancho.view.viewer.actions;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Scale;

class SetMinDynamicColumnWidthAction$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final Scale val$scale;
   // $VF: synthetic field
   private final SetMinDynamicColumnWidthAction$MinWidthDialog this$0;

   SetMinDynamicColumnWidthAction$1(SetMinDynamicColumnWidthAction$MinWidthDialog var1, Scale var2) {
      this.this$0 = var1;
      this.val$scale = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      int var2 = this.val$scale.getSelection();
      this.this$0.spinner.setSelection(var2);
   }
}
