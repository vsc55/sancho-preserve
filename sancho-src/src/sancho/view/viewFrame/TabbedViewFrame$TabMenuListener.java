package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.viewFrame.actions.AddTabAction;
import sancho.view.viewFrame.actions.RemoveTabAction;
import sancho.view.viewFrame.actions.TabsOnTopAction;

class TabbedViewFrame$TabMenuListener implements IMenuListener {
   String tabPrefString;
   TabbedViewFrame viewFrame;

   public TabbedViewFrame$TabMenuListener(TabbedViewFrame var1, String var2) {
      this.viewFrame = var1;
      this.tabPrefString = var2;
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new AddTabAction(this.viewFrame));
      if (this.viewFrame.getCTabFolder().getItemCount() > 1) {
         var1.add(new RemoveTabAction(this.viewFrame));
      }

      var1.add(new Separator());
      var1.add(new TabsOnTopAction(this.tabPrefString, this.viewFrame));
   }
}
