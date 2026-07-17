package sancho.view.transfer.downloads;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import sancho.core.Sancho;
import sancho.model.mldonkey.File;
import sancho.view.utility.SResources;

class DownloadTreeMenuListener$AddMirrorAction extends Action {
   File file;
   // $VF: synthetic field
   private final DownloadTreeMenuListener this$0;

   public DownloadTreeMenuListener$AddMirrorAction(DownloadTreeMenuListener var1, File var2) {
      super(SResources.getString("dd.f.addMirror"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
      this.file = var2;
   }

   public void run() {
      InputDialog var1 = new InputDialog(
         DownloadTreeMenuListener.access$2500(this.this$0).getShell(),
         SResources.getString("dd.f.addMirror"),
         SResources.getString("dd.f.addMirrorInfo"),
         "",
         null
      );
      var1.open();
      String var2 = var1.getValue();
      if (var2 != null) {
         Sancho.send((short)29, "mirror " + this.file.getId() + " " + var2);
      }
   }
}
