package sancho.model.mldonkey;

import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.MyObservable;
import sancho.utility.SwissArmy;

public class ConsoleMessage extends MyObservable implements IObject {
   private final int MAX_SIZE = 262136;
   private StringBuffer message = new StringBuffer();

   public synchronized String getMessage() {
      String var1 = this.message.toString();
      this.message = new StringBuffer();
      return var1;
   }

   public void read(MessageBuffer var1) {
      String var2 = null;
      synchronized (this) {
         if (this.message.length() > 262136) {
            this.message.setLength(0);
         }

         String var4 = SwissArmy.disableUTF8 ? var1.getString(false) : var1.getString();
         this.message.append(var4);
         if (this.countObservers() > 0) {
            var2 = this.message.toString();
            this.message = new StringBuffer();
         }
      }

      if (var2 != null) {
         this.setChanged();
         this.notifyObservers(var2);
      }
   }
}
