package sancho.view.statistics;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

public class InfoViewListener extends SashViewListener {
   public InfoViewListener(SashViewFrame sashViewFrame) {
      super(sashViewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      this.createSashActions(menuManager, "tab.statistics.tooltip");
   }
}
