package sancho.view.utility;

import org.eclipse.jface.action.Action;

class LinkRipper$DownloadAllAction extends Action {
   // $VF: synthetic field
   private final LinkRipper this$0;

   public LinkRipper$DownloadAllAction(LinkRipper var1) {
      super(SResources.getString("mi.downloadAll"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
   }

   public void run() {
      this.this$0.downloadAll();
   }
}
