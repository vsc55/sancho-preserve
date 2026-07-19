package sancho.model.mldonkey.utility;

import sancho.model.mldonkey.enums.EnumMessage;

public class RoomMessage {
   protected String message;
   protected int fromInt;
   protected int roomNumber;
   protected EnumMessage enumMessage;

   public synchronized String getMessage() {
      return this.message;
   }

   public synchronized EnumMessage getMessageType() {
      return this.enumMessage;
   }

   public synchronized int getFrom() {
      return this.fromInt;
   }

   public synchronized int getRoomNumber() {
      return this.roomNumber;
   }

   public void read(MessageBuffer buffer) {
      synchronized (this) {
         this.roomNumber = buffer.getInt32();
         this.enumMessage = EnumMessage.intToEnum(buffer.getInt8());
         if (this.enumMessage != EnumMessage.SERVER) {
            this.fromInt = buffer.getInt32();
         }

         this.message = buffer.getString();
      }
   }
}
