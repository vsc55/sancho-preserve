package sancho.view.statistics.networks;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Network;
import sancho.view.utility.SResources;

public class NetworksTableMenuListener$GetStatsAction extends Action {
   // $VF: synthetic field
   private final NetworksTableMenuListener this$0;

   public NetworksTableMenuListener$GetStatsAction(NetworksTableMenuListener var1) {
      super(SResources.getString("mi.getStats"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
   }

   public void run() {
      for (int var1 = 0; var1 < NetworksTableMenuListener.access$000(this.this$0).size(); var1++) {
         Network var2 = (Network)NetworksTableMenuListener.access$100(this.this$0).get(var1);
         var2.getStats();
      }
   }
}
