package sancho.core;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.utility.Splash;

class CoreFactory$7 implements Runnable {
   // $VF: synthetic field
   private final String val$title;
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$7(CoreFactory var1, String var2, String var3) {
      this.this$0 = var1;
      this.val$title = var2;
      this.val$message = var3;
   }

   public void run() {
      Splash.setVisible(false);
      CoreFactory$8 var1 = new CoreFactory$8(this, (Shell)null, VersionInfo.getName() + this.val$title, this.val$message, "", (IInputValidator)null);
      if (var1.open() == 0) {
         this.this$0.sResult = var1.getValue();
      } else {
         this.this$0.sResult = null;
      }

      Splash.setVisible(true);
   }
}
