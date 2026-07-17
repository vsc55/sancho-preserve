package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Result25 extends Result {
   Result25(ICore var1) {
      super(var1);
   }

   protected long readSize(MessageBuffer var1) {
      return var1.getUInt64();
   }
}
