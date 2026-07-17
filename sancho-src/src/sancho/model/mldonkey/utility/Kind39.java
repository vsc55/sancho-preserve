package sancho.model.mldonkey.utility;

import sancho.core.ICore;

public class Kind39 extends Kind {
   Kind39(ICore var1) {
      super(var1);
   }

   public void readAddr(MessageBuffer var1) {
      this.addr.read(false, var1);
      this.port = var1.getUInt16();
   }
}
