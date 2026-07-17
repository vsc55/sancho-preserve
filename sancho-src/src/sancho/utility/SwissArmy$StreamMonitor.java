package sancho.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class SwissArmy$StreamMonitor extends Thread {
   InputStream inputStream;

   SwissArmy$StreamMonitor(InputStream var1) {
      this.inputStream = var1;
   }

   public void run() {
      try {
         BufferedReader var1 = new BufferedReader(new InputStreamReader(this.inputStream));

         while (var1.readLine() != null) {
         }

         var1.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }
}
