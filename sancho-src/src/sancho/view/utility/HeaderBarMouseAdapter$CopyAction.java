package sancho.view.utility;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;

public class HeaderBarMouseAdapter$CopyAction extends Action {
   // $VF: synthetic field
   private final HeaderBarMouseAdapter this$0;

   public HeaderBarMouseAdapter$CopyAction(HeaderBarMouseAdapter var1) {
      super(SResources.getString("mi.copy"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      MainWindow.copyToClipboard(HeaderBarMouseAdapter.access$000(this.this$0).getText());
   }
}
