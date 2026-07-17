package sancho.view.utility;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

class DNDBox$ExitAction extends Action {
   Shell shell;

   public DNDBox$ExitAction(Shell var1) {
      super(SResources.getString("menu.file.exit"));
      this.shell = var1;
   }

   public void run() {
      this.shell.close();
   }
}
