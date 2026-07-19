package sancho.view.transfer.clients;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.File;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class ClientTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Client;

   public ClientTableMenuListener(ClientTableView view) {
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
         CustomTableViewer viewer = (CustomTableViewer)this.gView.getViewer();
         Object input = viewer.getInput();
         if (!(input instanceof File)) {
            input = null;
         }

         this.addClientActions(this.gView.getShell(), (File)input, menuManager, this.createClientArray());
         this.addSelectAllMenu(menuManager);
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
}
