package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$CancelAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$CancelAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.cancel"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("cancel"));
   }

   public void run() {
      DownloadTreeMenuListener.access$2400(this.this$0);
   }
}
