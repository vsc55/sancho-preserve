package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class SharedFile25 extends SharedFile {
   SharedFile25(ICore core) {
      super(core);
   }

   protected long readSize(MessageBuffer buffer) {
      return buffer.getUInt64();
   }
}
