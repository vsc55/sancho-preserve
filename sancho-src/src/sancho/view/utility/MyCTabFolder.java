package sancho.view.utility;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class MyCTabFolder extends CTabFolder {
   MyCTabFolder(Composite var1, int var2) {
      super(var1, var2);
   }

   public Rectangle getClientArea() {
      Rectangle var1 = super.getClientArea();
      if (this.getTabHeight() == 0) {
         if (this.getTabPosition() == 1024) {
            var1.height++;
         } else {
            var1.y = 0;
            var1.height++;
         }
      }

      return var1;
   }
}
