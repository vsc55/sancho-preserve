package sancho.view.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import sancho.core.Sancho;
import sancho.utility.MyObservable;

class ExecConsole$StreamMonitor extends MyObservable implements Runnable {
   private InputStream inputStream;
   private boolean keepAlive;
   private int type;
   // $VF: synthetic field
   private final ExecConsole this$0;

   public ExecConsole$StreamMonitor(ExecConsole var1, InputStream var2, int var3) {
      this.this$0 = var1;
      this.keepAlive = true;
      this.inputStream = var2;
      this.type = var3;
   }

   public int getType() {
      return this.type;
   }

   public void run() {
      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(this.inputStream));

         String var1;
         while (this.keepAlive && (var1 = var2.readLine()) != null) {
            if (!ExecConsole.access$200(this.this$0) && var1.toLowerCase().indexOf("core started") > -1) {
               ExecConsole.access$202(this.this$0, true);
            }

            this.setChanged();
            this.notifyObservers(var1);
         }

         var2.close();
      } catch (IOException var3) {
         Sancho.pDebug("streamMonitor:" + var3);
      }
   }

   public void stop() {
      this.keepAlive = false;
   }
}
