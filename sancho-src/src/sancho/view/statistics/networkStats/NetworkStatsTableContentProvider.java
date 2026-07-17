package sancho.view.statistics.networkStats;

import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.view.viewer.table.GTableContentProvider;

public class NetworkStatsTableContentProvider extends GTableContentProvider {
   public NetworkStatsTableContentProvider(NetworkStatsTableView var1) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      NetworkStatCollection var2 = (NetworkStatCollection)var1;
      return var2.getNetworkStatArray();
   }
}
