package sancho.view.viewer.table;

import java.io.File;
import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.SResources;

class GTableMenuListenerClient$PreviewAppAction extends Action {
   IPreview iPreview;
   String app;
   int subFileNum;
   // $VF: synthetic field
   private final GTableMenuListenerClient this$0;

   public GTableMenuListenerClient$PreviewAppAction(GTableMenuListenerClient var1, IPreview var2, String var3, int var4) {
      super(new File(var3).getName());
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("preview"));
      this.iPreview = var2;
      this.app = var3;
      this.subFileNum = var4;
   }

   public void run() {
      this.this$0.sendToStatusline(this.iPreview.preview(this.app, this.subFileNum));
   }
}
