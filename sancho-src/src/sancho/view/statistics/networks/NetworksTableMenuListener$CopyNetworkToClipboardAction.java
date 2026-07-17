package sancho.view.statistics.networks;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Network;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class NetworksTableMenuListener$CopyNetworkToClipboardAction extends Action {
   // $VF: synthetic field
   private final NetworksTableMenuListener this$0;

   public NetworksTableMenuListener$CopyNetworkToClipboardAction(NetworksTableMenuListener var1) {
      super(SResources.getString("mi.copy"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      StringBuffer var1 = new StringBuffer(50);
      String var2 = System.getProperty("line.separator");

      for (int var3 = 0; var3 < NetworksTableMenuListener.access$200(this.this$0).size(); var3++) {
         Network var4 = (Network)NetworksTableMenuListener.access$300(this.this$0).get(var3);
         if (var3 > 0) {
            var1.append(var2);
         }

         var1.append(var4.toString());
      }

      MainWindow.copyToClipboard(var1.toString());
   }
}
