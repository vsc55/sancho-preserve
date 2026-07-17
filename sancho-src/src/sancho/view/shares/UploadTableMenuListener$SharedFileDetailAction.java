package sancho.view.shares;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.SharedFile;
import sancho.view.utility.SResources;

class UploadTableMenuListener$SharedFileDetailAction extends Action {
   // $VF: synthetic field
   private final UploadTableMenuListener this$0;

   public UploadTableMenuListener$SharedFileDetailAction(UploadTableMenuListener var1) {
      super(SResources.getString("m.d.fileDetails"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("info"));
   }

   public void run() {
      SharedFile var1 = (SharedFile)UploadTableMenuListener.access$000(this.this$0).get(0);
      if (var1 != null) {
         new SharedFileDetailDialog(UploadTableMenuListener.access$100(this.this$0).getShell(), var1).open();
      }
   }
}
