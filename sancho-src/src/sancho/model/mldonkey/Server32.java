package sancho.model.mldonkey;

import sancho.core.ICore;

public class Server32 extends Server29 {
   Server32(ICore core) {
      super(core);
   }

   public void togglePreferred() {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), Byte.valueOf((byte)(this.isPreferred() ? 0 : 1))};
      this.core.send((short)67, args);
   }

   public void rename(String name) {
      Object[] args = new Object[]{Integer.valueOf(this.getId()), name};
      this.core.send((short)66, args);
   }
}
