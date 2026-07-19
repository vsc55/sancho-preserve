package sancho.view.statistics.networks;

import sancho.view.viewer.table.GTableContentProvider;

public class NetworksTableContentProvider extends GTableContentProvider {
   public NetworksTableContentProvider(NetworksTableView view) {
      super(view);
   }

   public Object[] getElements(Object input) {
      return (Object[])input;
   }
}
