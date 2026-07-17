package sancho.view.utility;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

class DNDBox$HideRestoreAction extends Action {
   Shell shell;

   public DNDBox$HideRestoreAction(Shell var1) {
      super(SResources.getString(var1.isVisible() ? "mi.hide" : "mi.restore"));
      this.setImageDescriptor(SResources.getImageDescriptor(var1.isVisible() ? "minus" : "plus"));
      this.shell = var1;
   }

   public void run() {
      this.shell.setVisible(!this.shell.isVisible());
   }
}
