package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class ClientMessage {
   private int id;
   private String messageText;

   public synchronized int getId() {
      return this.id;
   }

   public synchronized String getText() {
      return this.messageText;
   }

   public synchronized void read(MessageBuffer buffer) {
      this.id = buffer.getInt32();
      this.messageText = buffer.getString();
   }

   public static void sendMessage(ICore core, int id, String text) {
      Object[] args = new Object[]{Integer.valueOf(id), text};
      core.send((short)43, args);
   }
}
