package sancho.view.search.result;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class ResultViewFrame$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final ResultViewFrame this$0;

   ResultViewFrame$2(ResultViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      Sancho.send((short)4);
   }
}
