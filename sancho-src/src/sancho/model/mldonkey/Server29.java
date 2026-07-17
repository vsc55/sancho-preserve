package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Server29 extends Server28 {
   Server29(ICore var1) {
      super(var1);
   }

   protected boolean readPreferred(MessageBuffer var1) {
      return var1.getBool();
   }
}
