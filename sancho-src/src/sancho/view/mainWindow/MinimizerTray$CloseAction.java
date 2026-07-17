package sancho.view.mainWindow;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class MinimizerTray$CloseAction extends Action {
   // $VF: synthetic field
   private final MinimizerTray this$0;

   public MinimizerTray$CloseAction(MinimizerTray var1) {
      super(SResources.getString("mi.close"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("x"));
   }

   public void run() {
      MinimizerTray.access$202(this.this$0, true);
      this.this$0.shell.close();
   }
}
