package sancho.view.preferences;

import java.io.File;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class GCJFileFieldEditor extends FileFieldEditor {
   private String[] extensions = null;
   private boolean alwaysValid = false;

   public GCJFileFieldEditor(String var1, String var2, boolean var3, Composite var4) {
      super(var1, var2, var3, var4);
   }

   public GCJFileFieldEditor(String var1, String var2, boolean var3, Composite var4, boolean var5) {
      this(var1, var2, var3, var4);
      this.alwaysValid = var5;
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

      File var3 = this.getFile(var2);
      return var3 == null ? null : var3.getAbsolutePath();
   }

   private File getFile(File var1) {
      FileDialog var2 = new FileDialog(this.getShell(), 4096);
      if (var1 != null) {
         var2.setFileName(var1.getPath());
      }

      if (this.extensions != null) {
         var2.setFilterExtensions(this.extensions);
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

   public void setFileExtensions(String[] var1) {
      this.extensions = var1;
   }

   public boolean isValid() {
      return this.alwaysValid ? true : super.isValid();
   }

   protected boolean checkState() {
      return this.alwaysValid ? true : super.checkState();
   }
}
