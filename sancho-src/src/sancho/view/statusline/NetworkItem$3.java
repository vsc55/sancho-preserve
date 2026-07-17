package sancho.view.statusline;

import org.eclipse.swt.widgets.ToolItem;
import sancho.model.mldonkey.Network;

class NetworkItem$3 implements Runnable {
   // $VF: synthetic field
   private final Network val$network;
   // $VF: synthetic field
   private final NetworkItem this$0;

   NetworkItem$3(NetworkItem var1, Network var2) {
      this.this$0 = var1;
      this.val$network = var2;
   }

   public void run() {
      if (NetworkItem.access$000(this.this$0) != null && !NetworkItem.access$000(this.this$0).isDisposed()) {
         ToolItem var1 = NetworkItem.access$100(this.this$0, this.val$network);
         if (var1 != null && !var1.isDisposed()) {
            var1.setImage(this.val$network.getImage());
            var1.setToolTipText(this.val$network.getToolTip());
         }
      }
   }
}
