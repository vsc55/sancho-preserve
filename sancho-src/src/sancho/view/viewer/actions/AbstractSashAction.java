package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.SashForm;

public abstract class AbstractSashAction extends Action {
   protected SashForm sashForm;

   public AbstractSashAction(SashForm sashForm) {
      this.sashForm = sashForm;
   }
}
