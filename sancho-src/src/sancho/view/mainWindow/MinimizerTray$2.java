package sancho.view.mainWindow;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.preferences.PreferenceLoader;

class MinimizerTray$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final MinimizerTray this$0;

   MinimizerTray$2(MinimizerTray var1) {
      this.this$0 = var1;
   }

   public void widgetDefaultSelected(SelectionEvent var1) {
      if (!PreferenceLoader.loadBoolean("systraySingleClick")) {
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

   public void widgetSelected(SelectionEvent var1) {
      if (PreferenceLoader.loadBoolean("systraySingleClick")) {
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
}
