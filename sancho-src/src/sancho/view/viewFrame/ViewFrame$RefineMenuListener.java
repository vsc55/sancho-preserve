package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

class ViewFrame$RefineMenuListener implements IMenuListener {
   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ViewFrame$ToggleRefineAction("mi.refineFilterNegation", "refineFilterNegation"));
      var1.add(new ViewFrame$ToggleRefineAction("mi.refineFilterAlternates", "refineFilterAlternates"));
   }
}
