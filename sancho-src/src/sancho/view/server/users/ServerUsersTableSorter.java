package sancho.view.server.users;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.User;
import sancho.view.viewer.GSorter;

public class ServerUsersTableSorter extends GSorter {
   public ServerUsersTableSorter(ServerUsersTableView var1) {
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
         default:
            return 0;
      }
   }
}
