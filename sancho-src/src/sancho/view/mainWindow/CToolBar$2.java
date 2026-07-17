package sancho.view.mainWindow;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class CToolBar$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final CToolBar this$0;

   CToolBar$2(CToolBar var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CToolBar.access$100(this.this$0).configureTabs();
   }
}
