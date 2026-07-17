package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile25 extends SharedFile {
   SharedFile25(ICore var1) {
      super(var1);
   }

   protected long readSize(MessageBuffer var1) {
      return var1.getUInt64();
   }
}
