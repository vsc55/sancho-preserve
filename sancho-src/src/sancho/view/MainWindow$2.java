package sancho.view;

import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.File;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.AbstractTab;
import sancho.view.utility.DownloadCompleteDialog;

class MainWindow$2 implements Runnable {
   // $VF: synthetic field
   private final Object val$obj;
   // $VF: synthetic field
   private final MainWindow this$0;

   MainWindow$2(MainWindow var1, Object var2) {
      this.this$0 = var1;
      this.val$obj = var2;
   }

   public void run() {
      if (MainWindow.access$300(this.this$0) != null && !MainWindow.access$300(this.this$0).isDisposed()) {
         if (this.val$obj instanceof File) {
            if (PreferenceLoader.loadBoolean("downloadCompleteDialog")) {
               if (MainWindow.access$400(this.this$0) == null) {
                  MainWindow.access$402(this.this$0, new DownloadCompleteDialog(new Shell(), this.this$0));
                  MainWindow.access$400(this.this$0).open();
               }

               MainWindow.access$400(this.this$0).addFile((File)this.val$obj);
            }
         } else if (this.val$obj instanceof Boolean) {
            boolean var1 = this.val$obj == Boolean.TRUE;

            for (Object var3o : MainWindow.access$500(this.this$0)) { AbstractTab var3 = (AbstractTab)var3o;
               if (var1) {
                  var3.onConnect();
               } else {
                  var3.onDisconnect();
               }
            }

            if (MainWindow.access$600(this.this$0) != null) {
               MainWindow.access$600(this.this$0).setConnected(var1);
            }

            if (MainWindow.access$700(this.this$0) != null) {
               MainWindow.access$700(this.this$0).setConnected(var1);
            }

            if (MainWindow.access$800(this.this$0) != null) {
               MainWindow.access$800(this.this$0).setConnected(var1);
            }
         } else if (this.val$obj instanceof String && MainWindow.access$600(this.this$0) != null) {
            MainWindow.access$600(this.this$0).setText((String)this.val$obj);
         }
      }
   }
}
