package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.utility.WebLauncher;

class MenuBar$URLListener implements Listener {
   private String url;

   public MenuBar$URLListener(String var1) {
      this.url = var1;
   }

   public void handleEvent(Event var1) {
      WebLauncher.openLink(this.url);
   }
}
