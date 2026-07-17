package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Server;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

class ServerTableMenuListener$CopyServerLink extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$CopyServerLink(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.copyTo"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      String var1 = "";
      String var2 = System.getProperty("line.separator");

      for (int var3 = 0; var3 < ServerTableMenuListener.access$500(this.this$0).size(); var3++) {
         Server var4 = (Server)ServerTableMenuListener.access$600(this.this$0).get(var3);
         if (var1.length() > 0) {
            var1 = var1 + var2;
         }

         var1 = var1 + var4.getLink();
      }

      MainWindow.copyToClipboard(var1);
   }
}
