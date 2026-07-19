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

   public NetworksTableMenuListener(NetworksTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Network == null
            ? (class$sancho$model$mldonkey$Network = class$("sancho.model.mldonkey.Network"))
            : class$sancho$model$mldonkey$Network
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new CopyNetworkToClipboardAction());
         if (this.gView.getCore().getProtocol() >= 41) {
            menuManager.add(new GetStatsAction());
         }
      }
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Context-menu action: copies the selected networks to the clipboard.
   private class CopyNetworkToClipboardAction extends Action {
      public CopyNetworkToClipboardAction() {
         super(SResources.getString("mi.copy"));
         this.setImageDescriptor(SResources.getImageDescriptor("copy"));
      }

      public void run() {
         StringBuffer buffer = new StringBuffer(50);
         String lineSeparator = System.getProperty("line.separator");

         for (int i = 0; i < NetworksTableMenuListener.this.selectedObjects.size(); i++) {
            Network network = (Network)NetworksTableMenuListener.this.selectedObjects.get(i);
            if (i > 0) {
               buffer.append(lineSeparator);
            }

            buffer.append(network.toString());
         }

         MainWindow.copyToClipboard(buffer.toString());
      }
   }

   // Context-menu action: requests fresh stats for the selected networks.
   private class GetStatsAction extends Action {
      public GetStatsAction() {
         super(SResources.getString("mi.getStats"));
         this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
      }

      public void run() {
         for (int i = 0; i < NetworksTableMenuListener.this.selectedObjects.size(); i++) {
            Network network = (Network)NetworksTableMenuListener.this.selectedObjects.get(i);
            network.getStats();
         }
      }
   }
}
