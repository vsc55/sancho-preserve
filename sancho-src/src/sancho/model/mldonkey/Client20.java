package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class Client20 extends Client19 {
   protected int connectedTime;

   public Client20(ICore var1) {
      super(var1);
   }

   public synchronized int getConnectedTime() {
      return this.connectedTime;
   }

   public synchronized String getConnectedTimeString() {
      return SwissArmy.calcStringOfSeconds((long)this.getConnectedTime());
   }

   protected boolean readMore(MessageBuffer var1) {
      boolean var2 = super.readMore(var1);
      this.connectedTime = var1.getInt32();
      return var2;
   }
}
