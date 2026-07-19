package sancho.view.statistics.networks;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.Network;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;
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
         var1.add(new CopyNetworkToClipboardAction());
         if (this.gView.getCore().getProtocol() >= 41) {
            var1.add(new GetStatsAction());
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

   // Context-menu action: copies the selected networks to the clipboard.
   private class CopyNetworkToClipboardAction extends Action {
      public CopyNetworkToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         StringBuffer var1 = new StringBuffer(50);
         String var2 = System.getProperty("line.separator");

         for (int var3 = 0; var3 < NetworksTableMenuListener.this.selectedObjects.size(); var3++) {
            Network var4 = (Network)NetworksTableMenuListener.this.selectedObjects.get(var3);
            if (var3 > 0) {
               var1.append(var2);
            }

            var1.append(var4.toString());
         }

         MainWindow.copyToClipboard(var1.toString());
      }
   }

   // Context-menu action: requests fresh stats for the selected networks.
   private class GetStatsAction extends Action {
      public GetStatsAction() {
         super(SResources.getString("mi.getStats"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      }

      public void run() {
         for (int var1 = 0; var1 < NetworksTableMenuListener.this.selectedObjects.size(); var1++) {
            Network var2 = (Network)NetworksTableMenuListener.this.selectedObjects.get(var1);
            var2.getStats();
         }
      }
   }
}
