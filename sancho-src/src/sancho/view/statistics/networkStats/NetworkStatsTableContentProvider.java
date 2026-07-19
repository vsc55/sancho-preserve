package sancho.view.statistics.networkStats;

import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.view.viewer.table.GTableContentProvider;

public class NetworkStatsTableContentProvider extends GTableContentProvider {
   public NetworkStatsTableContentProvider(NetworkStatsTableView tableView) {
      super(tableView);
   }

   public Object[] getElements(Object input) {
      NetworkStatCollection collection = (NetworkStatCollection)input;
      return collection.getNetworkStatArray();
   }
}
