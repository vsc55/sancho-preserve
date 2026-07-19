package sancho.view.preferences;

import java.io.File;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

public class GCJDirectoryFieldEditor extends DirectoryFieldEditor {
   public GCJDirectoryFieldEditor(String name, String labelText, Composite parent) {
      super(name, labelText, parent);
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

      File directory = this.getDirectory(file);
      return directory == null ? null : directory.getAbsolutePath();
   }

   private File getDirectory(File startingDirectory) {
      DirectoryDialog dialog = new DirectoryDialog(this.getShell(), 4096);
      if (startingDirectory != null) {
         dialog.setFilterPath(startingDirectory.getPath());
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
}
