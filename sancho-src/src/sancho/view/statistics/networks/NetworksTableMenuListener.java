package sancho.view.statistics.networks;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListener;

public class NetworksTableMenuListener extends GTableMenuListener implements ISelectionChangedListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Network;

   public NetworksTableMenuListener(NetworksTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Network == null
            ? (class$sancho$model$mldonkey$Network = class$("sancho.model.mldonkey.Network"))
            : class$sancho$model$mldonkey$Network
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new NetworksTableMenuListener$CopyNetworkToClipboardAction(this));
         if (this.gView.getCore().getProtocol() >= 41) {
            var1.add(new NetworksTableMenuListener$GetStatsAction(this));
         }
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $VF: synthetic method
   static List access$000(NetworksTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(NetworksTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$200(NetworksTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$300(NetworksTableMenuListener var0) {
      return var0.selectedObjects;
   }
}
