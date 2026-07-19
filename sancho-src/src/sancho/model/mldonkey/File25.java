package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class File25 extends File24 {
   File25(ICore core) {
      super(core);
   }

   protected long readDownloaded(MessageBuffer buffer) {
      return buffer.getUInt64();
   }

   protected long readSize(MessageBuffer buffer) {
      return buffer.getUInt64();
   }
}
