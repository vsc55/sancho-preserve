package sancho.view.search.result;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class ResultViewFrame$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final ResultViewFrame this$0;

   ResultViewFrame$3(ResultViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = ResultViewFrame.access$100(this.this$0).getSelection();
      if (var2 != null) {
         ResultTab var3 = (ResultTab)var2.getData();
         if (var3 != null) {
            if (var3.isPaused()) {
               this.this$0.togglePauseContinue(false);
               var3.unPause();
            } else {
               var3.pause();
               this.this$0.togglePauseContinue(true);
            }
         }
      }
   }
}
