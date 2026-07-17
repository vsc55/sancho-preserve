package sancho.view.viewFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

class TabbedViewFrame$2 implements Listener {
   // $VF: synthetic field
   private final TabbedViewFrame this$0;

   TabbedViewFrame$2(TabbedViewFrame var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if (!SWT.getPlatform().equals("fox") || var1.button == 3) {
         Point var2 = this.this$0.cTabFolder.getDisplay().getCursorLocation();
         var2 = this.this$0.cTabFolder.toControl(var2);
         CTabItem var3 = this.this$0.cTabFolder.getItem(var2);
         if (var3 != null) {
            Menu var4 = this.this$0.popupMenu.createContextMenu(this.this$0.cTabFolder);
            var4.setVisible(true);
         }
      }
   }
}
