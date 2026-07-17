package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$ConnectAllAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$ConnectAllAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.connectAll"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$600(this.this$0).size(); var1++) {
         ((File)DownloadTreeMenuListener.access$700(this.this$0).get(var1)).connectAll();
      }
   }
}
