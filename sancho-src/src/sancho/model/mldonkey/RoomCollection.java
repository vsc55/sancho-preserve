package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.RoomMessage;
import sancho.model.mldonkey.utility.UtilityFactory;
import gnu.trove.TObjectProcedure;
import java.util.ArrayList;
import java.util.List;
import sancho.model.mldonkey.enums.EnumRoomState;

public class RoomCollection extends ACollection_Int2 {
   RoomCollection(ICore core) {
      super(core);
   }

   public void addUser(MessageBuffer buffer) {
      int roomId = buffer.getInt32();
      int userId = buffer.getInt32();
      if (this.containsKey(roomId)) {
         Room room = (Room)this.get(roomId);
         if (this.core.getUserCollection().containsKey(userId)) {
            room.addUser((User)this.core.getUserCollection().get(userId));
         } else {
            Sancho.pDebug("RDE");
         }
      }
   }

   public Room[] getAllOpenRooms() {
      GetAllOpenRooms collector = new GetAllOpenRooms();
      this.forEachValue(collector);
      Room[] rooms = new Room[collector.getRoomList().size()];
      collector.getRoomList().toArray(rooms);
      return rooms;
   }

   public Room getRoom(int id) {
      return (Room)super.get(id);
   }

   public void read(MessageBuffer buffer) {
      int id = buffer.getInt32();
      Room room = (Room)this.get(id);
      if (room != null) {
         room.read(id, buffer);
         this.addToUpdated(room);
      } else {
         room = this.core.getCollectionFactory().getRoom();
         room.read(id, buffer);
         this.put(id, room);
         this.addToAdded(room);
      }

      this.setChanged();
      this.notifyObservers(room);
   }

   public void removeUser(MessageBuffer buffer) {
      int roomId = buffer.getInt32();
      int userId = buffer.getInt32();
      if (this.containsKey(roomId)) {
         Room room = (Room)this.get(roomId);
         if (this.core.getUserCollection().containsKey(userId)) {
            room.removeUser((User)this.core.getUserCollection().get(userId));
         }
      }
   }

   public void roomMessage(MessageBuffer buffer) {
      RoomMessage message = UtilityFactory.getRoomMessage(this.core);
      message.read(buffer);
      this.setChanged();
      this.notifyObservers(message);
   }

   // Trove forEachValue: collect the rooms currently in the OPEN state.
   private static class GetAllOpenRooms implements TObjectProcedure {
      List roomList = new ArrayList();

      public boolean execute(Object value) {
         Room room = (Room)value;
         if (room.getRoomState() == EnumRoomState.OPEN) {
            this.roomList.add(room);
         }

         return true;
      }

      public List getRoomList() {
         return this.roomList;
      }
   }
}
