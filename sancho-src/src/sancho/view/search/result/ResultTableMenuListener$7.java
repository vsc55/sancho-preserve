package sancho.view.search.result;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.viewer.actions.WebServicesAction;

class ResultTableMenuListener$7 extends SelectionAdapter {
   // $VF: synthetic field
   private final int val$type;
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$7(ResultTableMenuListener$ToolTipHandler var1, int var2) {
      this.this$1 = var1;
      this.val$type = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      WebServicesAction.launch(
         this.val$type,
         ResultTableMenuListener$ToolTipHandler.access$1400(this.this$1),
         ResultTableMenuListener$ToolTipHandler.access$1500(this.this$1),
         ResultTableMenuListener$ToolTipHandler.access$1600(this.this$1)
      );
      ResultTableMenuListener$ToolTipHandler.access$1700(this.this$1, false);
   }
}
