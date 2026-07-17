package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Server28 extends Server {
   Server28(ICore var1) {
      super(var1);
   }

   protected long readNUsers(MessageBuffer var1) {
      return var1.getUInt64();
   }

   protected long readNFiles(MessageBuffer var1) {
      return var1.getUInt64();
   }
}
