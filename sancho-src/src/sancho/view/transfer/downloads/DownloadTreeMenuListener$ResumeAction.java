package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$ResumeAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$ResumeAction(DownloadTreeMenuListener var1) {
      super(SResources.getString("m.d.resume"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("resume"));
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$2200(this.this$0).size(); var1++) {
         File var2 = (File)DownloadTreeMenuListener.access$2300(this.this$0).get(var1);
         if (var2.getFileStateEnum() == EnumFileState.PAUSED) {
            var2.setState(EnumFileState.DOWNLOADING);
         }
      }
   }
}
