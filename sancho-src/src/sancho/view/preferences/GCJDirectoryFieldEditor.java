package sancho.view.preferences;

import java.io.File;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

public class GCJDirectoryFieldEditor extends DirectoryFieldEditor {
   public GCJDirectoryFieldEditor(String var1, String var2, Composite var3) {
      super(var1, var2, var3);
   }

   protected String changePressed() {
      String var1 = this.getTextControl().getText();
      if (var1.equals("") && SWT.getPlatform().equals("win32")) {
         var1 = ".";
      }

      File var2 = new File(var1);
      if (!var2.exists()) {
         var2 = null;
      }

      File var3 = this.getDirectory(var2);
      return var3 == null ? null : var3.getAbsolutePath();
   }

   private File getDirectory(File var1) {
      DirectoryDialog var2 = new DirectoryDialog(this.getShell(), 4096);
      if (var1 != null) {
         var2.setFilterPath(var1.getPath());
      }

      String var3 = var2.open();
      if (var3 != null) {
         var3 = var3.trim();
         if (var3.length() > 0) {
            return new File(var3);
         }
      }

      return null;
   }
}
