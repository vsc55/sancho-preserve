package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class RemoveTabAction extends Action {
   TabbedViewFrame viewFrame;

   public RemoveTabAction(TabbedViewFrame viewFrame) {
      super(SResources.getString("mi.d.removeTab"));
      this.setImageDescriptor(SResources.getImageDescriptor("minus"));
      this.viewFrame = viewFrame;
   }

   public void run() {
      CTabFolder tabFolder = this.viewFrame.getCTabFolder();
      if (tabFolder.getItemCount() > 1) {
         CTabItem tabItem = tabFolder.getSelection();
         int index = tabFolder.indexOf(tabItem) - 1;
         if (tabItem != null && !tabItem.isDisposed()) {
            tabItem.dispose();
         }

         if (index < 0) {
            index = 0;
         }

         tabItem = tabFolder.getItems()[index];
         this.viewFrame.switchToTab(tabItem);
      }
   }
}
