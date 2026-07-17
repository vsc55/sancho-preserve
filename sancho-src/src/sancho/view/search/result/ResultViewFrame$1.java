package sancho.view.search.result;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

class ResultViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final ResultViewFrame this$0;

   ResultViewFrame$1(ResultViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem[] var2 = ResultViewFrame.access$000(this.this$0).getItems();

      for (int var3 = 0; var3 < var2.length; var3++) {
         CTabItem var4 = var2[var3];
         Control var5 = var4.getControl();
         if (var5 != null && !var5.isDisposed()) {
            var5.dispose();
         }

         var4.dispose();
      }
   }
}
