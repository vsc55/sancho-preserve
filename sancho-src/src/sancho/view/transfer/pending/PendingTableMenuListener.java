package sancho.view.transfer.pending;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.view.viewer.table.GTableMenuListenerClient;
import sancho.view.viewer.table.GTableView;

public class PendingTableMenuListener extends GTableMenuListenerClient {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Client;

   public PendingTableMenuListener(GTableView var1) {
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
         this.addClientActions(this.gView.getShell(), null, var1, this.createClientArray());
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
