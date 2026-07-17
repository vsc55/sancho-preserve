package sancho.view.shares;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.utility.SResources;

class UploadViewFrame$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final UploadViewFrame this$0;

   UploadViewFrame$1(UploadViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.shareDialog = new UploadViewFrame$ShareDialog(
         this.this$0, UploadViewFrame.access$000(this.this$0).getShell(), SResources.getString("l.unshareDirectory"), false
      );
      if (this.this$0.shareDialog.open() == 0) {
         this.this$0.sendShareCommand(false);
      }

      this.this$0.shareDialog = null;
   }
}
