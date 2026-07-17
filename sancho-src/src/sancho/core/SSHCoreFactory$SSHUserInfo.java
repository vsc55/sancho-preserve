package sancho.core;

import com.jcraft.jsch.UserInfo;
import sancho.utility.VersionInfo;

public class SSHCoreFactory$SSHUserInfo implements UserInfo {
   String passwd;
   // $VF: synthetic field
   private final SSHCoreFactory this$0;

   public SSHCoreFactory$SSHUserInfo(SSHCoreFactory var1) {
      this.this$0 = var1;
   }

   public String getPassphrase() {
      return this.passwd;
   }

   public String getPassword() {
      return this.passwd;
   }

   public boolean promptPassphrase(String var1) {
      String var2 = this.this$0.askForPassword("/SSH", var1);
      this.this$0.sResult = null;
      if (var2 == null) {
         return false;
      } else {
         this.passwd = var2;
         return true;
      }
   }

   public boolean promptPassword(String var1) {
      String var2 = this.this$0.askForPassword("/SSH", var1);
      this.this$0.sResult = null;
      if (var2 == null) {
         return false;
      } else {
         this.passwd = var2;
         return true;
      }
   }

   public boolean promptYesNo(String var1) {
      return this.this$0.createYesNoBox(VersionInfo.getName() + "/SSH", var1);
   }

   public void showMessage(String var1) {
      this.this$0.display.syncExec(new SSHCoreFactory$1(this, var1));
   }
}
