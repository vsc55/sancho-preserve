package sancho.view.utility;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

class LinkRipper$5 extends MouseAdapter {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$5(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void mouseDoubleClick(MouseEvent var1) {
      String[] var2 = this.this$0.urlList.getSelection();
      String var3 = "";
      if (var2.length > 0) {
         var3 = var2[0];
      }

      if (this.this$0.urlList.getSelectionCount() == 1 && var3.toLowerCase().startsWith("ftp") && var3.endsWith("/")) {
         this.this$0.urlText.setText(var3);
         this.this$0.ripLinks();
      } else {
         this.this$0.downloadSelected();
      }
   }
}
