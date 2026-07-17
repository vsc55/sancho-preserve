package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$ChgrpAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$ChgrpAction(DownloadTreeMenuListener var1) {
      super("chgrp");
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("group"));
   }

   public void run() {
      InputDialog var1 = new InputDialog(DownloadTreeMenuListener.access$2900(this.this$0).getShell(), "chgrp", "chgrp", "", null);
      var1.open();
      String var2 = var1.getValue();

      for (int var3 = 0; var3 < DownloadTreeMenuListener.access$3000(this.this$0).size(); var3++) {
         File var4 = (File)DownloadTreeMenuListener.access$3100(this.this$0).get(var3);
         var4.chgrp(var2);
      }
   }
}
