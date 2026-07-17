package sancho.view.transfer;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;

class AbstractDetailDialog$1 implements MenuListener {
   // $VF: synthetic field
   private final CLabel val$cLabel;
   // $VF: synthetic field
   private final AbstractDetailDialog this$0;

   AbstractDetailDialog$1(AbstractDetailDialog var1, CLabel var2) {
      this.this$0 = var1;
      this.val$cLabel = var2;
   }

   public void menuHidden(MenuEvent var1) {
      this.val$cLabel.setBackground(this.val$cLabel.getDisplay().getSystemColor(22));
   }

   public void menuShown(MenuEvent var1) {
      this.val$cLabel.setBackground(this.val$cLabel.getDisplay().getSystemColor(26));
   }
}
