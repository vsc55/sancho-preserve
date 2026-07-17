package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.core.Sancho;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.model.mldonkey.utility.RoomMessage;
import sancho.model.mldonkey.utility.UtilityFactory;

public class RoomCollection extends ACollection_Int2 {
   RoomCollection(ICore var1) {
      super(var1);
   }

   public void addUser(MessageBuffer var1) {
      int var2 = var1.getInt32();
      int var3 = var1.getInt32();
      if (this.containsKey(var2)) {
         Room var4 = (Room)this.get(var2);
         if (this.core.getUserCollection().containsKey(var3)) {
            var4.addUser((User)this.core.getUserCollection().get(var3));
         } else {
            Sancho.pDebug("RDE");
         }
      }
   }

   public Room[] getAllOpenRooms() {
      RoomCollection$GetAllOpenRooms var1 = new RoomCollection$GetAllOpenRooms();
      this.forEachValue(var1);
      Room[] var2 = new Room[var1.getRoomList().size()];
      var1.getRoomList().toArray(var2);
      return var2;
   }

   public Room getRoom(int var1) {
      return (Room)super.get(var1);
   }

   public void read(MessageBuffer var1) {
      int var2 = var1.getInt32();
      Room var3 = (Room)this.get(var2);
      if (var3 != null) {
         var3.read(var2, var1);
         this.addToUpdated(var3);
      } else {
         var3 = this.core.getCollectionFactory().getRoom();
         var3.read(var2, var1);
         this.put(var2, var3);
         this.addToAdded(var3);
      }

      this.setChanged();
      this.notifyObservers(var3);
   }

   public void removeUser(MessageBuffer var1) {
      int var2 = var1.getInt32();
      int var3 = var1.getInt32();
      if (this.containsKey(var2)) {
         Room var4 = (Room)this.get(var2);
         if (this.core.getUserCollection().containsKey(var3)) {
            var4.removeUser((User)this.core.getUserCollection().get(var3));
         }
      }
   }

   public void roomMessage(MessageBuffer var1) {
      RoomMessage var2 = UtilityFactory.getRoomMessage(this.core);
      var2.read(var1);
      this.setChanged();
      this.notifyObservers(var2);
   }
}
