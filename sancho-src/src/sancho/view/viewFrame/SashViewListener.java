package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.SashForm;
import sancho.view.viewer.actions.FlipSashAction;
import sancho.view.viewer.actions.MaximizeAction;

public abstract class SashViewListener extends ViewListener {
   protected SashForm sashForm;

   public SashViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
      this.sashForm = viewFrame.getParentSashForm();
   }

   public void createSashActions(IMenuManager menuManager, String prefString) {
      menuManager.add(new Separator());
      menuManager.add(new FlipSashAction(this.sashForm));
      menuManager.add(new MaximizeAction(this.sashForm, this.control, prefString));
   }
}
