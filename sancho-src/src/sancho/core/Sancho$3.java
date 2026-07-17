package sancho.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.swt.widgets.Shell;
import sancho.view.utility.dialogs.BugDialog;

class Sancho$3 implements Runnable {
   // $VF: synthetic field
   private final String val$string;
   // $VF: synthetic field
   private final Exception val$e;

   Sancho$3(String var1, Exception var2) {
      this.val$string = var1;
      this.val$e = var2;
   }

   public void run() {
      StringWriter var1 = new StringWriter();
      var1.write("From Thread: " + this.val$string + "\n\n");
      if (this.val$e != null) {
         this.val$e.printStackTrace(new PrintWriter(var1, true));
      } else {
         var1.write("NULL EXCEPTION");
      }

      new BugDialog(new Shell(Sancho.access$300()), var1.toString()).open();
   }
}
