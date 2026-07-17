package sancho.view;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

class MainWindow$1 implements Listener {
   // $VF: synthetic field
   private final MainWindow this$0;

   MainWindow$1(MainWindow var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      if ((var1.stateMask & 262144) != 0 || (var1.stateMask & 65536) != 0) {
         if ((var1.stateMask & 65536) != 0) {
            if (var1.keyCode == 16777219) {
               int var2 = MainWindow.access$000(this.this$0) - 1;
               if (var2 < 0) {
                  var2 = MainWindow.access$100(this.this$0) - 1;
               }

               MainWindow.access$200(this.this$0, var2);
            } else if (var1.keyCode == 16777220) {
               int var3 = MainWindow.access$000(this.this$0) + 1;
               if (var3 >= MainWindow.access$100(this.this$0)) {
                  var3 = 0;
               }

               MainWindow.access$200(this.this$0, var3);
            }
         }

         if (var1.keyCode >= 49 && var1.keyCode <= 57) {
            int var4 = var1.keyCode - 48;
            if (var4 <= MainWindow.access$100(this.this$0)) {
               MainWindow.access$200(this.this$0, var4 - 1);
            }
         }

         if ((var1.stateMask & 262144) != 0 && var1.keyCode == 116) {
            this.this$0.sendTorrentsFromHD();
         }
      }
   }
}
