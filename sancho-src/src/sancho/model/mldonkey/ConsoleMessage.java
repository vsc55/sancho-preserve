package sancho.model.mldonkey;

import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.MyObservable;
import sancho.utility.SwissArmy;

public class ConsoleMessage extends MyObservable implements IObject {
   private final int MAX_SIZE = 262136;
   private StringBuffer message = new StringBuffer();

   public synchronized String getMessage() {
      String text = this.message.toString();
      this.message = new StringBuffer();
      return text;
   }

   public void read(MessageBuffer buffer) {
      String notification = null;
      synchronized (this) {
         if (this.message.length() > 262136) {
            this.message.setLength(0);
         }

         String chunk = SwissArmy.disableUTF8 ? buffer.getString(false) : buffer.getString();
         this.message.append(chunk);
         if (this.countObservers() > 0) {
            notification = this.message.toString();
            this.message = new StringBuffer();
         }
      }

      if (notification != null) {
         this.setChanged();
         this.notifyObservers(notification);
      }
   }
}
