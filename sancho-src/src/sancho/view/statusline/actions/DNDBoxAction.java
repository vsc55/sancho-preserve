package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class DNDBoxAction extends Action {
   MainWindow mainWindow;

   public DNDBoxAction(MainWindow mainWindow) {
      super(SResources.getString("l.toggleDNDBox"));
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      this.mainWindow = mainWindow;
   }

   public void run() {
      this.mainWindow.toggleDNDBox();
   }
}
