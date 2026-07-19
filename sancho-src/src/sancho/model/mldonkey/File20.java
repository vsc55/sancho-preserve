package sancho.model.mldonkey;

import sancho.core.ICore;

public class File20 extends File18 {
   File20(ICore core) {
      super(core);
   }

   public void rename(String name) {
      this.core.send((short)56, new Object[]{Integer.valueOf(this.getId()), name});
   }
}
