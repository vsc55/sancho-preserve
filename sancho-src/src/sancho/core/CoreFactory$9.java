package sancho.core;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

class CoreFactory$9 extends MessageDialog {
   CoreFactory$9(Shell var1, String var2, Image var3, String var4, int var5, String[] var6, int var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   protected void setShellStyle(int var1) {
      super.setShellStyle(var1 & -65537);
   }
}
