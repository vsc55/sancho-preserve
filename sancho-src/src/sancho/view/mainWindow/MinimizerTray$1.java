package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

class MinimizerTray$1 implements Listener {
   // $VF: synthetic field
   private final MinimizerTray this$0;

   MinimizerTray$1(MinimizerTray var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      Menu var2 = MinimizerTray.access$000(this.this$0).createContextMenu(this.this$0.shell);
      var2.setVisible(true);
   }
}
