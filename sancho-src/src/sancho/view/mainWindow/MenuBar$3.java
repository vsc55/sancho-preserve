package sancho.view.mainWindow;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.core.Sancho;
import sancho.utility.SwissArmy;
import sancho.view.utility.SResources;

class MenuBar$3 implements Listener {
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$3(MenuBar$1 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      InputDialog var2 = new InputDialog(
         MenuBar.access$100(MenuBar$1.access$000(this.this$1)).getShell(),
         SResources.getString("menu.file.inputLink"),
         SResources.getString("menu.file.inputLink"),
         "",
         null
      );
      var2.open();
      String var3 = var2.getValue();
      if (var3 != null && !var3.equals("") && Sancho.getCore() != null) {
         SwissArmy.sendLink(Sancho.getCore(), var3);
      }
   }
}
