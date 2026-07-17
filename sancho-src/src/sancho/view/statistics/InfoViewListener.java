package sancho.view.statistics;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class InfoViewListener extends SashViewListener {
   public InfoViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createSashActions(var1, "tab.statistics.tooltip");
   }
}
