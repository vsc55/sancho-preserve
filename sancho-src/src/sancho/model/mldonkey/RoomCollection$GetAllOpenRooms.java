package sancho.model.mldonkey;

import gnu.trove.TObjectProcedure;
import java.util.ArrayList;
import java.util.List;
import sancho.model.mldonkey.enums.EnumRoomState;

class RoomCollection$GetAllOpenRooms implements TObjectProcedure {
   List roomList = new ArrayList();

   public boolean execute(Object var1) {
      Room var2 = (Room)var1;
      if (var2.getRoomState() == EnumRoomState.OPEN) {
         this.roomList.add(var2);
      }

      return true;
   }

   public List getRoomList() {
      return this.roomList;
   }
}
