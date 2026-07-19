package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class Kind39 extends Kind {
   Kind39(ICore core) {
      super(core);
   }

   public void readAddr(MessageBuffer buffer) {
      this.addr.read(false, buffer);
      this.port = buffer.getUInt16();
   }
}
