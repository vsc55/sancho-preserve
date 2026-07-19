package sancho.model.mldonkey;

import sancho.core.ICore;

public class File21 extends File20 {
   File21(ICore core) {
      super(core);
   }

   protected boolean checkFileNum(Client client) {
      return client.isTransferring(this.getId());
   }
}
