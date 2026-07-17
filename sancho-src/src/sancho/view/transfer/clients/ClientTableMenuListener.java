package sancho.view.transfer.clients;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.CustomTableViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.File;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class ClientTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Client;

   public ClientTableMenuListener(ClientTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Client == null
            ? (class$sancho$model$mldonkey$Client = class$("sancho.model.mldonkey.Client"))
            : class$sancho$model$mldonkey$Client
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         CustomTableViewer var2 = (CustomTableViewer)this.gView.getViewer();
         Object var3 = var2.getInput();
         if (!(var3 instanceof File)) {
            var3 = null;
         }

         this.addClientActions(this.gView.getShell(), (File)var3, var1, this.createClientArray());
         this.addSelectAllMenu(var1);
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
}
