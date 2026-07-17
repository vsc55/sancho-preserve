package sancho.model.mldonkey;

import sancho.core.ICore;

public class File21 extends File20 {
   File21(ICore var1) {
      super(var1);
   }

   protected boolean checkFileNum(Client var1) {
      return var1.isTransferring(this.getId());
   }
}
