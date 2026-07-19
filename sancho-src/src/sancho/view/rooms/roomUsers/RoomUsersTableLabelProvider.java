package sancho.view.rooms.roomUsers;

import sancho.model.mldonkey.User;
import sancho.view.viewer.table.GTableLabelProvider;

public class RoomUsersTableLabelProvider extends GTableLabelProvider {
   public RoomUsersTableLabelProvider(RoomUsersTableView view) {
      super(view);
   }

   public String getColumnText(Object element, int columnIndex) {
      User user = (User)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return user.getName();
         case 1:
            return user.getTagsString();
         case 2:
            return user.getAddr().toString();
         case 3:
            return String.valueOf(user.getPort()).intern();
         case 4:
            return String.valueOf(user.getServerId()).intern();
         default:
            return "??";
      }
   }
}
