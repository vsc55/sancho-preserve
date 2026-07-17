package sancho.core;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;

class CoreFactory$8 extends InputDialog {
   // $VF: synthetic field
   private final CoreFactory$7 this$1;

   CoreFactory$8(CoreFactory$7 var1, Shell var2, String var3, String var4, String var5, IInputValidator var6) {
      super(var2, var3, var4, var5, var6);
      this.this$1 = var1;
   }

   protected Control createDialogArea(Composite var1) {
      Control var2 = super.createDialogArea(var1);
      this.getText().setEchoChar('*');
      return var2;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
   }
}
