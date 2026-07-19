package sancho.view.rooms.roomUsers;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.User;
import sancho.view.viewer.GSorter;

public class RoomUsersTableSorter extends GSorter {
   public RoomUsersTableSorter(RoomUsersTableView view) {
      super(view);
   }

   protected int _compare(Viewer viewer, Object object1, Object object2, int columnId) {
      User user1 = (User)object1;
      User user2 = (User)object2;
      switch (columnId) {
         case 0:
            return this.compareStrings(user1.getName(), user2.getName());
         case 1:
            return this.compareStrings(user1.getTagsString(), user2.getTagsString());
         case 2:
            return this.compareAddrs(user1.getAddr(), user2.getAddr());
         case 3:
            return this.compareInts(user1.getPort(), user2.getPort());
         case 4:
            return this.compareInts(user1.getServerId(), user2.getServerId());
         default:
            return this.compareDefault((TableViewer)viewer, this.columnIndex, object1, object2);
      }
   }
}
