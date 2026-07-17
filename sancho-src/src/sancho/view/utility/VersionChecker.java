package sancho.view.utility;

import java.net.URL;
import org.eclipse.swt.widgets.Shell;
import sancho.view.StatusLine;

public class VersionChecker implements Runnable {
   URL url;
   Shell shell;
   String newVersion;
   StatusLine statusLine;

   public VersionChecker(Shell var1, StatusLine var2, int var3) {
      this.shell = var1;
      this.statusLine = var2;
      var1.getDisplay().timerExec(var3, this);
   }

   public void run() {
      Thread var1 = new Thread(new VersionChecker$1(this));
      var1.setDaemon(true);
      var1.start();
   }
}
