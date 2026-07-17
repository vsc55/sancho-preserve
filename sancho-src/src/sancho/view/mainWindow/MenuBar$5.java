package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class MenuBar$5 implements Listener {
   // $VF: synthetic field
   private final MenuBar$1 this$1;

   MenuBar$5(MenuBar$1 var1) {
      this.this$1 = var1;
   }

   public void handleEvent(Event var1) {
      MessageBox var2 = new MessageBox(MenuBar.access$200(MenuBar$1.access$000(this.this$1)), 196);
      var2.setMessage(SResources.getString("mi.areYouSure"));
      if (var2.open() == 64 && MenuBar.access$100(MenuBar$1.access$000(this.this$1)).getCore() != null) {
         Sancho.send((short)3);
      }
   }
}
