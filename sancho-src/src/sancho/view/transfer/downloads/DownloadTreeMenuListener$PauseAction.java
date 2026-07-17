package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$PauseAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$PauseAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.pause"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("pause"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$1000(this.this$0).size(); var1++) {
         File var2 = (File)DownloadTreeMenuListener.access$1100(this.this$0).get(var1);
         if (var2.getFileStateEnum() != EnumFileState.PAUSED) {
            var2.setState(EnumFileState.PAUSED);
         }
      }
   }
}
