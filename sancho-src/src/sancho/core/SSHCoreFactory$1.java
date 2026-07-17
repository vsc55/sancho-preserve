package sancho.core;

import sancho.utility.VersionInfo;
import sancho.view.utility.Splash;

class SSHCoreFactory$1 implements Runnable {
   // $VF: synthetic field
   private final String val$message;
   // $VF: synthetic field
   private final SSHCoreFactory$SSHUserInfo this$1;

   SSHCoreFactory$1(SSHCoreFactory$SSHUserInfo var1, String var2) {
      this.this$1 = var1;
      this.val$message = var2;
   }

   public void run() {
      Splash.setVisible(false);
      CoreFactory.openInformation(null, VersionInfo.getName() + "/SSH", this.val$message);
      Splash.setVisible(true);
   }
}
