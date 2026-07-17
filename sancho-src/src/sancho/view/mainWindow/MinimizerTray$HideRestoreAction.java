package sancho.view.mainWindow;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class MinimizerTray$HideRestoreAction extends Action {
   // $VF: synthetic field
   private final MinimizerTray this$0;

   public MinimizerTray$HideRestoreAction(MinimizerTray var1) {
      super(SResources.getString(var1.shell.isVisible() ? "mi.hide" : "mi.restore"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor(var1.shell.isVisible() ? "minus" : "plus"));
   }

   public void run() {
      if (this.this$0.shell.isVisible()) {
         this.this$0.hide();
      } else {
         this.this$0.restore();
         if (this.this$0.shell.getMinimized()) {
            this.this$0.shell.setMinimized(false);
         }
      }
   }
}
