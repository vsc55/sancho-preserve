package sancho.view.shares;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class UploadViewFrame$3 extends SelectionAdapter {
   // $VF: synthetic field
   private final UploadViewFrame this$0;

   UploadViewFrame$3(UploadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         this.this$0.getCore().getSharedFileCollection().reshare();
      }
   }
}
