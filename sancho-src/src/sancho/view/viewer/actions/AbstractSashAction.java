package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.SashForm;

public abstract class AbstractSashAction extends Action {
   protected SashForm sashForm;

   public AbstractSashAction(SashForm var1) {
      this.sashForm = var1;
   }
}
