package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;
import sancho.utility.SwissArmy;

public class Client21 extends Client20 {
   protected String emuleMod;

   public Client21(ICore core) {
      super(core);
   }

   public synchronized String getConnectedTimeString() {
      return this.connectedTime > 76340000 ? "-" : SwissArmy.calcStringOfSeconds((long)this.connectedTime);
   }

   public synchronized String getSoftware() {
      return this.emuleMod != null && this.emuleMod.length() > 0 ? super.getSoftware() + "(" + this.emuleMod + ")" : super.getSoftware();
   }

   protected boolean readMore(MessageBuffer buffer) {
      boolean changed = super.readMore(buffer);
      this.emuleMod = buffer.getString();
      return changed;
   }
}
