package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.transfer.FileDetailDialog;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$FileDetailAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$FileDetailAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.fileDetails"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("info"));
   }

   public void run() {
      if (DownloadTreeMenuListener.access$000(this.this$0) != null) {
         new FileDetailDialog(DownloadTreeMenuListener.access$800(this.this$0).getShell(), DownloadTreeMenuListener.access$000(this.this$0)).open();
      }
   }
}
