package sancho.view.viewFrame;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.utility.NoDuplicatesCombo;

class ViewFrame$4 extends SelectionAdapter {
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$4(ViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      NoDuplicatesCombo var2 = (NoDuplicatesCombo)var1.widget;
      if (var2.getSelectionIndex() > -1) {
         this.this$0.refineText.setText(var2.getItem(var2.getSelectionIndex()));
      }

      if (this.this$0.getGView() != null) {
         this.this$0.getGView().setRefineString(this.this$0.refineText.getText());
      }
   }
}
