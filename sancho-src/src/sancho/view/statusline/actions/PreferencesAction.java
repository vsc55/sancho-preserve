package sancho.view.statusline.actions;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class PreferencesAction extends Action {
   MainWindow mainWindow;

   public PreferencesAction(MainWindow var1) {
      super(SResources.getString("menu.tools.preferences"));
      this.setImageDescriptor(SResources.getImageDescriptor("preferences"));
      this.mainWindow = var1;
   }

   public void run() {
      this.mainWindow.openPreferences();
   }
}
