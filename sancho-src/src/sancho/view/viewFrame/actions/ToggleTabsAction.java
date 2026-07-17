package sancho.view.viewFrame.actions;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;
import sancho.view.viewFrame.TabbedViewFrame;

public class ToggleTabsAction extends Action {
   TabbedViewFrame viewFrame;

   public ToggleTabsAction(TabbedViewFrame var1) {
      super(SResources.getString("mi.toggleTabs"));
      this.setImageDescriptor(SResources.getImageDescriptor("toggle"));
      this.viewFrame = var1;
   }

   public void run() {
      this.viewFrame.toggleTabs();
   }
}
