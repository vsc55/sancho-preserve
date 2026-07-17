package sancho.view.utility;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

class LinkRipper$4 implements Listener {
   // $VF: synthetic field
   private final LinkRipper this$0;

   LinkRipper$4(LinkRipper var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if (var1.button == 3) {
         Menu var2 = this.this$0.popupMenu.createContextMenu(this.this$0.urlList);
         var2.setLocation(this.this$0.urlList.getDisplay().getCursorLocation());
         var2.setVisible(true);
      }
   }
}
