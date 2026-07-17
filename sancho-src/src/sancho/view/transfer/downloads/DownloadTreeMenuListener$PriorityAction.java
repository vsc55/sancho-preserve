package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.File;
import sancho.model.mldonkey.enums.EnumFileState;
import sancho.model.mldonkey.enums.EnumPriority;

class DownloadTreeMenuListener$PriorityAction extends Action {
   private EnumPriority enumPriority;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$PriorityAction(DownloadTreeMenuListener var1, EnumPriority var2) {
      super(var2.getName().toLowerCase(), 2);
      this.this$0 = var1;
      this.enumPriority = var2;
   }

   public void run() {
      for (int var1 = 0; var1 < DownloadTreeMenuListener.access$3200(this.this$0).size(); var1++) {
         File var2 = (File)DownloadTreeMenuListener.access$3300(this.this$0).get(var1);
         if (var2.getFileStateEnum() != EnumFileState.DOWNLOADED) {
            var2.sendPriority(this.enumPriority);
         }
      }
   }

   public boolean isChecked() {
      return DownloadTreeMenuListener.access$000(this.this$0).getPriorityEnum() == this.enumPriority;
   }
}
