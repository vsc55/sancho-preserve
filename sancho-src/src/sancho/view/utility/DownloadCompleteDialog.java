package sancho.view.utility;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sancho.model.mldonkey.File;
import sancho.utility.VersionInfo;
import sancho.view.MainWindow;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class DownloadCompleteDialog extends Dialog {
   MainWindow mainWindow;
   Text textInfo;

   public DownloadCompleteDialog(Shell parentShell, MainWindow mainWindow) {
      super(parentShell);
      this.mainWindow = mainWindow;
      this.setBlockOnOpen(false);
   }

   public int getShellStyle() {
      return super.getShellStyle() | 16 | 16384;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(SResources.getString("l.downloadComplete"));
      shell.setImage(VersionInfo.getProgramIcon());
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      this.getShell().setBounds(PreferenceLoader.loadRectangle("downloadCompleteWindowBounds"));
   }

   public void addFile(File file) {
      this.textInfo.append("[" + file.getId() + "] " + file.getName() + " - " + file.getED2K() + this.textInfo.getLineDelimiter());
      this.getShell().setText(SResources.getString("l.downloadComplete") + " (" + (this.textInfo.getLineCount() - 1) + ")");
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      this.textInfo = new Text(composite, 2826);
      this.textInfo.setLayoutData(new GridData(1808));
      return composite;
   }

   protected void createButtonsForButtonBar(Composite parent) {
      this.createButton(parent, 0, SResources.getString("b.ok"), true);
   }

   public boolean close() {
      this.mainWindow.closeDownloadCompleteDialog();
      PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "downloadCompleteWindowBounds", this.getShell().getBounds());
      PreferenceLoader.saveStore();
      return super.close();
   }
}
