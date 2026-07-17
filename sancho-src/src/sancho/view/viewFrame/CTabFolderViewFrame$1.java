package sancho.view.viewFrame;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class CTabFolderViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final CTabFolderViewFrame this$0;

   CTabFolderViewFrame$1(CTabFolderViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.onCTabFolderSelection();
   }
}
