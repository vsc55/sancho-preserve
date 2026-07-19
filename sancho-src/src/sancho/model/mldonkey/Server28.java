package sancho.model.mldonkey;

import sancho.core.ICore;
import sancho.model.mldonkey.utility.MessageBuffer;

public class Server28 extends Server {
   Server28(ICore core) {
      super(core);
   }

   protected long readNUsers(MessageBuffer buffer) {
      return buffer.getUInt64();
   }

   protected long readNFiles(MessageBuffer buffer) {
      return buffer.getUInt64();
   }
}
