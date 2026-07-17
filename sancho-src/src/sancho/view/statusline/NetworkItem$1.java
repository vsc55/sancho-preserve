package sancho.view.statusline;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

class NetworkItem$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final MenuManager val$popupMenu;
   // $VF: synthetic field
   private final NetworkItem this$0;

   NetworkItem$1(NetworkItem var1, MenuManager var2) {
      this.this$0 = var1;
      this.val$popupMenu = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      Rectangle var2 = ((ToolItem)var1.widget).getBounds();
      Menu var3 = this.val$popupMenu.createContextMenu(NetworkItem.access$000(this.this$0));
      Point var4 = new Point(var2.x, var2.y + var2.height);
      var4 = NetworkItem.access$000(this.this$0).toDisplay(var4);
      var3.setLocation(var4.x, var4.y);
      var3.setVisible(true);
   }
}
