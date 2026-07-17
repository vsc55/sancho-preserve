package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$EditMP3TagsAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$EditMP3TagsAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.editMP3Tags"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("preferences"));
   }

   public void run() {
      if (DownloadTreeMenuListener.access$000(this.this$0) != null) {
         new EditMP3TagsDialog(DownloadTreeMenuListener.access$1900(this.this$0).getShell(), DownloadTreeMenuListener.access$000(this.this$0)).open();
      }
   }
}
