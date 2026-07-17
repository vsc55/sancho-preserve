package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.transfer.FileDetailDialog;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$FileCommentsAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$FileCommentsAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.fileComments"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("comments"));
   }

   public void run() {
      if (DownloadTreeMenuListener.access$000(this.this$0) != null) {
         new FileDetailDialog(DownloadTreeMenuListener.access$900(this.this$0).getShell(), DownloadTreeMenuListener.access$000(this.this$0), true).open();
      }
   }
}
