package sancho.view.viewer.table;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.SResources;

class GTableMenuListenerClient$PreviewAction extends Action {
   int[] subFileArray;
   IPreview[] iPreviewArray;
   // $VF: synthetic field
   private final GTableMenuListenerClient this$0;

   public GTableMenuListenerClient$PreviewAction(GTableMenuListenerClient var1, IPreview[] var2, int[] var3) {
      super(SResources.getString("m.d.preview"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("preview"));
      this.iPreviewArray = var2;
      this.subFileArray = var3;
   }

   public void run() {
      for (int var1 = 0; var1 < this.iPreviewArray.length; var1++) {
         for (int var2 = 0; var2 < this.subFileArray.length; var2++) {
            this.this$0.sendToStatusline(this.iPreviewArray[var1].preview(this.subFileArray[var2]));
         }
      }
   }
}
