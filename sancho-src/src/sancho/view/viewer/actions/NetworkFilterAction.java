package sancho.view.viewer.actions;

import sancho.model.mldonkey.Network;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.NetworkViewerFilter;

public class NetworkFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$NetworkViewerFilter;

   public NetworkFilterAction(GView gView, Network network) {
      super(network.getName(), 2, gView, network.getEnumNetwork());
      this.filterClass = class$sancho$view$viewer$filters$NetworkViewerFilter == null
         ? (class$sancho$view$viewer$filters$NetworkViewerFilter = class$("sancho.view.viewer.filters.NetworkViewerFilter"))
         : class$sancho$view$viewer$filters$NetworkViewerFilter;
   }

   public AbstractViewerFilter createNewFilter() {
      return new NetworkViewerFilter(this.gView);
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException classNotFound) {
         throw new NoClassDefFoundError(classNotFound.getMessage());
      }
   }
}
