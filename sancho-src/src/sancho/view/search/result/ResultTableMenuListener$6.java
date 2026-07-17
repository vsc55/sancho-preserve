package sancho.view.search.result;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class ResultTableMenuListener$6 extends SelectionAdapter {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   ResultTableMenuListener$6(ResultTableMenuListener$ToolTipHandler var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      ResultTableMenuListener$ToolTipHandler.access$1300(this.this$1).downloadSingleFile(ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1));
   }
}
