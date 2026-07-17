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

   public void read(MessageBuffer var1) {
      synchronized (this) {
         this.roomNumber = var1.getInt32();
         this.enumMessage = EnumMessage.intToEnum(var1.getInt8());
         if (this.enumMessage != EnumMessage.SERVER) {
            this.fromInt = var1.getInt32();
         }

         this.message = var1.getString();
      }
   }
}
