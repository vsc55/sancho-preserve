package sancho.view.viewFrame;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.custom.SashForm;
import sancho.view.viewer.actions.FlipSashAction;
import sancho.view.viewer.actions.MaximizeAction;

public abstract class TabbedSashViewListener extends ViewListener {
   protected SashForm sashForm;

   public TabbedSashViewListener(TabbedSashViewFrame var1) {
      super(var1);
      this.sashForm = var1.getParentSashForm();
   }

   public void createSashActions(IMenuManager var1, String var2) {
      var1.add(new Separator());
      var1.add(new FlipSashAction(this.sashForm));
      var1.add(new MaximizeAction(this.sashForm, this.control, var2));
   }
}
