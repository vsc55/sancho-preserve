package sancho.view.server.users;

import org.eclipse.swt.custom.CLabel;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.table.GTableContentProvider;

public class ServerUsersTableContentProvider extends GTableContentProvider {
   public ServerUsersTableContentProvider(ServerUsersTableView view, CLabel label) {
      super(view);
   }

   public Object[] getElements(Object inputElement) {
      if (inputElement instanceof Server) {
         Server server = (Server)inputElement;
         if (server.hasUsers()) {
            return server.getUsers();
         }
      }

      return GTableContentProvider.EMPTY_ARRAY;
   }
}
