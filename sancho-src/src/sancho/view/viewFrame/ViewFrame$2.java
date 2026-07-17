package sancho.view.viewFrame;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class ViewFrame$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final ViewFrame this$0;

   ViewFrame$2(ViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (this.this$0.refineText != null) {
         this.this$0.refineText.add(this.this$0.refineText.getText(), 0);
         this.this$0.refineText.setText("");
      }

      if (this.this$0.getGView() != null) {
         this.this$0.getGView().setRefineString("");
      }
   }
}
