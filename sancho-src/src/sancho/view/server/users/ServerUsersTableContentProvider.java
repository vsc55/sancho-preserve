package sancho.view.server.users;

import org.eclipse.swt.custom.CLabel;
import sancho.model.mldonkey.Server;
import sancho.view.viewer.table.GTableContentProvider;

public class ServerUsersTableContentProvider extends GTableContentProvider {
   public ServerUsersTableContentProvider(ServerUsersTableView var1, CLabel var2) {
      super(var1);
   }

   public Object[] getElements(Object var1) {
      if (var1 instanceof Server) {
         Server var2 = (Server)var1;
         if (var2.hasUsers()) {
            return var2.getUsers();
         }
      }

      return GTableContentProvider.EMPTY_ARRAY;
   }
}
