package sancho.view.utility;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class MyCTabFolder extends CTabFolder {
   MyCTabFolder(Composite composite, int style) {
      super(composite, style);
   }

   public Rectangle getClientArea() {
      Rectangle rect = super.getClientArea();
      if (this.getTabHeight() == 0) {
         if (this.getTabPosition() == 1024) {
            rect.height++;
         } else {
            rect.y = 0;
            rect.height++;
         }
      }

      return rect;
   }
}
