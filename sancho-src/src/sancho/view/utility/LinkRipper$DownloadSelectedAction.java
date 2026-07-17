package sancho.view.utility;

import org.eclipse.jface.action.Action;

class LinkRipper$DownloadSelectedAction extends Action {
   // $VF: synthetic field
   private final LinkRipper this$0;

   public LinkRipper$DownloadSelectedAction(LinkRipper var1) {
      super(SResources.getString("mi.downloadSelected"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_yellow"));
   }

   public void run() {
      this.this$0.downloadSelected();
   }
}
