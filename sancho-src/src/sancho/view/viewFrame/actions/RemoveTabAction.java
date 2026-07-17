package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class RemoveTabAction extends Action {
   TabbedViewFrame viewFrame;

   public RemoveTabAction(TabbedViewFrame var1) {
      super(SResources.getString("mi.d.removeTab"));
      this.setImageDescriptor(SResources.getImageDescriptor("minus"));
      this.viewFrame = var1;
   }

   public void run() {
      CTabFolder var1 = this.viewFrame.getCTabFolder();
      if (var1.getItemCount() > 1) {
         CTabItem var2 = var1.getSelection();
         int var3 = var1.indexOf(var2) - 1;
         if (var2 != null && !var2.isDisposed()) {
            var2.dispose();
         }

         if (var3 < 0) {
            var3 = 0;
         }

         var2 = var1.getItems()[var3];
         this.viewFrame.switchToTab(var2);
      }
   }
}
