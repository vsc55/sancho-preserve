package sancho.core;

import java.net.Socket;
import sancho.model.mldonkey.utility.MessageBuffer;

public class MLDonkeyCoreMonitor extends MLDonkeyCore {
   public MLDonkeyCoreMonitor(Socket socket, String username, String password, boolean pollMode) {
      super(socket, username, password, pollMode);
   }

   void processMessage(int opcode, MessageBuffer buffer) {
      switch (opcode) {
         case 0:
            this.readCoreProtocol(buffer);
            break;
         case 47:
            this.disconnect();
            this.semaphore = true;
            break;
         case 49:
            if (!this.initialized) {
               this.notifyInitialized();
            }

            this.getClientStats().read(buffer);
      }
   }
}
