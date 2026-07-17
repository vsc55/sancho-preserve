package sancho.view.viewer;

import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Menu;

class GView$1 extends MenuAdapter {
   // $VF: synthetic field
   private final GView this$0;

   GView$1(GView var1) {
      this.this$0 = var1;
   }

   public void menuShown(MenuEvent var1) {
      Menu var2 = this.this$0.getComposite().getMenu();
      if (!this.this$0.getViewer().getSelection().isEmpty() && var2.getItemCount() > 0) {
         var2.setDefaultItem(var2.getItem(0));
      }
   }
}
