package sancho.view.rooms.roomUsers;

import sancho.model.mldonkey.User;
import sancho.view.viewer.table.GTableLabelProvider;

public class RoomUsersTableLabelProvider extends GTableLabelProvider {
   public RoomUsersTableLabelProvider(RoomUsersTableView var1) {
      super(var1);
   }

   public String getColumnText(Object var1, int var2) {
      User var3 = (User)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return var3.getTagsString();
         case 2:
            return var3.getAddr().toString();
         case 3:
            return String.valueOf(var3.getPort()).intern();
         case 4:
            return String.valueOf(var3.getServerId()).intern();
         default:
            return "??";
      }
   }
}
