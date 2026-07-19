package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class Client20 extends Client19 {
   protected int connectedTime;

   public Client20(ICore core) {
      super(core);
   }

   public synchronized int getConnectedTime() {
      return this.connectedTime;
   }

   public synchronized String getConnectedTimeString() {
      return SwissArmy.calcStringOfSeconds((long)this.getConnectedTime());
   }

   protected boolean readMore(MessageBuffer buffer) {
      boolean changed = super.readMore(buffer);
      this.connectedTime = buffer.getInt32();
      return changed;
   }
}
