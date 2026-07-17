package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$RequestFileInfoAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$RequestFileInfoAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.requestFileInfo"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$2000(this.this$0).size(); var1++) {
         File var2 = (File)DownloadTreeMenuListener.access$2100(this.this$0).get(var1);
         var2.requestFileInfo();
      }
   }
}
