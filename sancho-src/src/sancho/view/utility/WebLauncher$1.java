package sancho.view.utility;

import java.io.IOException;
import org.eclipse.swt.widgets.Display;

class WebLauncher$1 extends Thread {
   // $VF: synthetic field
   private final String val$localHref;
   // $VF: synthetic field
   private final Display val$d;

   WebLauncher$1(String var1, String var2, Display var3) {
      super(var1);
      this.val$localHref = var2;
      this.val$d = var3;
   }

   public void run() {
      try {
         if (!WebLauncher.access$000()
            || !WebLauncher.access$100().equals("MozillaFirebird")
               && !WebLauncher.access$100().equals("netscape")
               && !WebLauncher.access$100().equals("mozilla")) {
            Process var11 = WebLauncher.access$200(this.val$localHref);
            WebLauncher.access$002(true);

            try {
               if (var11 != null) {
                  var11.waitFor();
               }
            } catch (InterruptedException var8) {
               WebLauncher.access$300(this.val$d);
            } finally {
               WebLauncher.access$002(false);
            }
         } else {
            String[] var1 = new String[]{WebLauncher.access$100(), "-remote", "openURL(" + this.val$localHref + ")"};
            Runtime.getRuntime().exec(var1);
         }
      } catch (IOException var10) {
         WebLauncher.access$300(this.val$d);
      }
   }
}
