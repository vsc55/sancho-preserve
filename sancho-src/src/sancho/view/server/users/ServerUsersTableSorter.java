package sancho.view.server.users;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.User;
import sancho.view.viewer.GSorter;

public class ServerUsersTableSorter extends GSorter {
   public ServerUsersTableSorter(ServerUsersTableView view) {
      super(view);
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnID) {
      User user1 = (User)element1;
      User user2 = (User)element2;
      switch (columnID) {
         case 0:
            return this.compareStrings(user1.getName(), user2.getName());
         case 1:
            return this.compareStrings(user1.getTagsString(), user2.getTagsString());
         case 2:
            return this.compareAddrs(user1.getAddr(), user2.getAddr());
         case 3:
            return this.compareInts(user1.getPort(), user2.getPort());
         default:
            return 0;
      }
   }
}
