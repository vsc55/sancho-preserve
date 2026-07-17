package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$ChownAction extends Action {
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$ChownAction(DownloadTreeMenuListener var1) {
      super("chown");
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("user"));
   }

   public void run() {
      InputDialog var1 = new InputDialog(DownloadTreeMenuListener.access$2600(this.this$0).getShell(), "chown", "chown", "", null);
      var1.open();
      String var2 = var1.getValue();

      for (int var3 = 0; var3 < DownloadTreeMenuListener.access$2700(this.this$0).size(); var3++) {
         File var4 = (File)DownloadTreeMenuListener.access$2800(this.this$0).get(var3);
         var4.chown(var2);
      }
   }
}
