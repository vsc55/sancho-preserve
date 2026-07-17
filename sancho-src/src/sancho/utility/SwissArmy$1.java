package sancho.utility;

import java.io.File;
import sancho.core.Sancho;

class SwissArmy$1 extends Thread {
   // $VF: synthetic field
   private final String val$workingDir;
   // $VF: synthetic field
   private final String[] val$cmdArray;

   SwissArmy$1(String var1, String[] var2) {
      this.val$workingDir = var1;
      this.val$cmdArray = var2;
   }

   public void run() {
      Runtime var1 = Runtime.getRuntime();

      try {
         File var3 = null;
         if (this.val$workingDir != null) {
            var3 = new File(this.val$workingDir);
         }

         Process var2;
         if (this.val$workingDir != null && var3 != null && var3.exists()) {
            var2 = var1.exec(this.val$cmdArray, null, var3);
         } else {
            var2 = var1.exec(this.val$cmdArray);
         }

         SwissArmy$StreamMonitor var4 = new SwissArmy$StreamMonitor(var2.getErrorStream());
         SwissArmy$StreamMonitor var5 = new SwissArmy$StreamMonitor(var2.getInputStream());
         var4.setDaemon(true);
         var4.start();
         var5.setDaemon(true);
         var5.start();
         var2.waitFor();
      } catch (Exception var6) {
         var6.printStackTrace();
         Sancho.pDebug("execInThread: " + var6);
      }
   }
}
