package sancho.view.rooms.roomUsers;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.User;
import sancho.view.viewer.GSorter;

public class RoomUsersTableSorter extends GSorter {
   public RoomUsersTableSorter(RoomUsersTableView var1) {
      super(var1);
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      User var5 = (User)var2;
      User var6 = (User)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareStrings(var5.getTagsString(), var6.getTagsString());
         case 2:
            return this.compareAddrs(var5.getAddr(), var6.getAddr());
         case 3:
            return this.compareInts(var5.getPort(), var6.getPort());
         case 4:
            return this.compareInts(var5.getServerId(), var6.getServerId());
         default:
            return this.compareDefault((TableViewer)var1, this.columnIndex, var2, var3);
      }
   }
}
