package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Client33 extends Client23 {
   public Client33(ICore core) {
      super(core);
   }

   protected boolean readMore(MessageBuffer buffer) {
      boolean changed = super.readMore(buffer);
      String extra = buffer.getString();
      if (extra.length() > 0) {
         this.software = this.software + " " + extra;
      }

      return changed;
   }
}
