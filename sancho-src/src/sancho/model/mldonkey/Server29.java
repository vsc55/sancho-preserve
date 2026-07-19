package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Server29 extends Server28 {
   Server29(ICore core) {
      super(core);
   }

   protected boolean readPreferred(MessageBuffer buffer) {
      return buffer.getBool();
   }
}
