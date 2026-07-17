package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File25 extends File24 {
   File25(ICore var1) {
      super(var1);
   }

   protected long readDownloaded(MessageBuffer var1) {
      return var1.getUInt64();
   }

   protected long readSize(MessageBuffer var1) {
      return var1.getUInt64();
   }
}
