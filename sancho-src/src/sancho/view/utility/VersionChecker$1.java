package sancho.view.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;

class VersionChecker$1 implements Runnable {
   // $VF: synthetic field
   private final VersionChecker this$0;

   VersionChecker$1(VersionChecker var1) {
      this.this$0 = var1;
   }

   public void run() {
      try {
         this.this$0.url = new URL(VersionInfo.getHomePage2() + "/version.php");
      } catch (MalformedURLException var5) {
         Sancho.pDebug("VersionChecker: " + var5);
         return;
      }

      try {
         URLConnection var1 = this.this$0.url.openConnection();
         StringBuffer var6 = new StringBuffer(64);
         var6.append(VersionInfo.getName());
         var6.append("/");
         var6.append(VersionInfo.getVersion());
         var6.append(" ");
         var6.append("(");
         var6.append(System.getProperty("os.name"));
         var6.append(" ");
         var6.append(System.getProperty("os.version"));
         var6.append("/");
         var6.append(VersionInfo.getSWTPlatform());
         var6.append(")");
         var6.append(" ");
         var6.append("(");
         var6.append(System.getProperty("java.vm.name"));
         var6.append(" ");
         var6.append(System.getProperty("java.vm.version"));
         var6.append(")");
         var1.setRequestProperty("User-Agent", var6.toString());
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
         this.this$0.newVersion = var3.readLine();
         var3.close();
         if (!this.this$0.shell.isDisposed() && !this.this$0.shell.getDisplay().isDisposed()) {
            this.this$0.shell.getDisplay().asyncExec(new VersionChecker$2(this));
         }
      } catch (IOException var4) {
         Sancho.pDebug("VersionChecker: " + var4);
         String var2 = var4.toString();
         this.this$0.shell.getDisplay().asyncExec(new VersionChecker$3(this));
      }
   }

   // $VF: synthetic method
   static VersionChecker access$000(VersionChecker$1 var0) {
      return var0.this$0;
   }
}
