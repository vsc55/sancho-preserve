package sancho.core;

import java.net.Socket;
import sancho.model.mldonkey.utility.MessageBuffer;

public class MLDonkeyCoreMonitor extends MLDonkeyCore {
   public MLDonkeyCoreMonitor(Socket var1, String var2, String var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   void processMessage(int var1, MessageBuffer var2) {
      switch (var1) {
         case 0:
            this.readCoreProtocol(var2);
            break;
         case 47:
            this.disconnect();
            this.semaphore = true;
            break;
         case 49:
            if (!this.initialized) {
               this.notifyInitialized();
            }

            this.getClientStats().read(var2);
      }
   }
}
