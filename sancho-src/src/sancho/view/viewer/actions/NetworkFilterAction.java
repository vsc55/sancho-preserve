package sancho.view.viewer.actions;

import sancho.model.mldonkey.Network;
import sancho.view.viewer.GView;
import sancho.view.viewer.filters.AbstractViewerFilter;
import sancho.view.viewer.filters.NetworkViewerFilter;

public class NetworkFilterAction extends AbstractFilterAction {
   // $VF: synthetic field
   static Class class$sancho$view$viewer$filters$NetworkViewerFilter;

   public NetworkFilterAction(GView var1, Network var2) {
      super(var2.getName(), 2, var1, var2.getEnumNetwork());
      this.filterClass = class$sancho$view$viewer$filters$NetworkViewerFilter == null
         ? (class$sancho$view$viewer$filters$NetworkViewerFilter = class$("sancho.view.viewer.filters.NetworkViewerFilter"))
         : class$sancho$view$viewer$filters$NetworkViewerFilter;
   }

   public AbstractViewerFilter createNewFilter() {
      return new NetworkViewerFilter(this.gView);
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
