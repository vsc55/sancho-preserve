package sancho.model.mldonkey;

import org.eclipse.swt.graphics.Image;
import sancho.core.ICore;
import sancho.model.mldonkey.enums.EnumNetwork;
import sancho.model.mldonkey.enums.EnumRoomState;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.ObjectMap;

public class Room extends AObject {
   protected EnumRoomState roomState;
   protected String name;
   protected int id;
   protected EnumNetwork networkEnum;
   protected ObjectMap userMap = null;

   Room(ICore core) {
      super(core);
   }

   public synchronized String getName() {
      return this.name != null ? this.name : "";
   }

   public synchronized EnumRoomState getRoomState() {
      return this.roomState;
   }

   public int getNumUsers() {
      return -1;
   }

   public synchronized int getId() {
      return this.id;
   }

   public synchronized EnumNetwork getEnumNetwork() {
      return this.networkEnum;
   }

   public synchronized String getNetworkName() {
      return this.networkEnum.getName();
   }

   public synchronized Image getNetworkImage() {
      return this.networkEnum.getImage();
   }

   public void read(MessageBuffer buffer) {
      this.read(buffer.getInt32(), buffer);
   }

   public void read(int id, MessageBuffer buffer) {
      synchronized (this) {
         this.id = id;
         this.networkEnum = this.readNetworkEnum(buffer);
         this.name = buffer.getString();
         this.roomState = EnumRoomState.intToEnum(buffer.getInt8());
      }
   }

   protected EnumNetwork readNetworkEnum(MessageBuffer buffer) {
      return this.core.getNetworkCollection().getNetworkEnum(buffer.getInt32());
   }

   public void addUser(User user) {
      this.getUserMap().add(user);
   }

   public void removeUser(User user) {
      this.getUserMap().remove(user);
   }

   public ObjectMap getUserMap() {
      if (this.userMap == null) {
         this.userMap = new ObjectMap(true);
      }

      return this.userMap;
   }

   public void close() {
      this.setRoomState(EnumRoomState.CLOSED);
   }

   public void open() {
      this.setRoomState(EnumRoomState.OPEN);
   }

   public void setRoomState(EnumRoomState state) {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), Byte.valueOf(state.getByteValue())};
      this.core.send((short)48, args);
   }
}
