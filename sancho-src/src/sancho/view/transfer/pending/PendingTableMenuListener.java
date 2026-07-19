package sancho.view.transfer.pending;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListenerClient;
import sancho.view.viewer.table.GTableView;

public class PendingTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Client;

   public PendingTableMenuListener(GTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Client == null
            ? (class$sancho$model$mldonkey$Client = class$("sancho.model.mldonkey.Client"))
            : class$sancho$model$mldonkey$Client
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         this.addClientActions(this.gView.getShell(), null, menuManager, this.createClientArray());
         this.addSelectAllMenu(menuManager);
      }
   }

   // $VF: synthetic method
   static Class class$(String name) {
      try {
         return Class.forName(name);
      } catch (ClassNotFoundException notFound) {
         throw new NoClassDefFoundError(notFound.getMessage());
      }
   }
}
