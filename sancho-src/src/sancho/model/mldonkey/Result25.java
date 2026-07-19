package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Result25 extends Result {
   Result25(ICore core) {
      super(core);
   }

   protected long readSize(MessageBuffer buffer) {
      return buffer.getUInt64();
   }
}
