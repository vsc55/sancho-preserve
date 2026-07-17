package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$VerifyChunksAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$VerifyChunksAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.verifyChunks"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("verify"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$400(this.this$0).size(); var1++) {
         ((File)DownloadTreeMenuListener.access$500(this.this$0).get(var1)).verifyChunks();
      }
   }
}
