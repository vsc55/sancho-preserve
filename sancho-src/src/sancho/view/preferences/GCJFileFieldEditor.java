package sancho.view.preferences;

import java.io.File;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

public class GCJFileFieldEditor extends FileFieldEditor {
   private String[] extensions = null;
   private boolean alwaysValid = false;

   public GCJFileFieldEditor(String name, String labelText, boolean enforceAbsolute, Composite parent) {
      super(name, labelText, enforceAbsolute, parent);
   }

   public GCJFileFieldEditor(String name, String labelText, boolean enforceAbsolute, Composite parent, boolean alwaysValid) {
      this(name, labelText, enforceAbsolute, parent);
      this.alwaysValid = alwaysValid;
   }

   protected String changePressed() {
      String text = this.getTextControl().getText();
      if (text.equals("") && SWT.getPlatform().equals("win32")) {
         text = ".";
      }

      File file = new File(text);
      if (!file.exists()) {
         file = null;
      }

      File selectedFile = this.getFile(file);
      return selectedFile == null ? null : selectedFile.getAbsolutePath();
   }

   private File getFile(File startingDirectory) {
      FileDialog dialog = new FileDialog(this.getShell(), 4096);
      if (startingDirectory != null) {
         dialog.setFileName(startingDirectory.getPath());
      }

      if (this.extensions != null) {
         dialog.setFilterExtensions(this.extensions);
      }

      String path = dialog.open();
      if (path != null) {
         path = path.trim();
         if (path.length() > 0) {
            return new File(path);
         }
      }

      return null;
   }

   public void setFileExtensions(String[] extensions) {
      this.extensions = extensions;
   }

   public boolean isValid() {
      return this.alwaysValid ? true : super.isValid();
   }

   protected boolean checkState() {
      return this.alwaysValid ? true : super.checkState();
   }
}
