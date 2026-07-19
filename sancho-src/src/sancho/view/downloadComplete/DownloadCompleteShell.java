package sancho.view.downloadComplete;

import java.io.File;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.utility.VersionInfo;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class DownloadCompleteShell extends Dialog {
   public DownloadCompleteShell(Shell shell) {
      super(shell);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
      shell.setText(SResources.getString("l.downloadCompleteTitle"));
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      this.getShell().setBounds(PreferenceLoader.loadRectangle("downloadHistoryWindowBounds"));
   }

   public int getShellStyle() {
      return super.getShellStyle() | 16;
   }

   protected void createButtonsForButtonBar(Composite composite) {
      ((GridLayout)composite.getLayout()).numColumns++;
      final Button button = new Button(composite, 0);
      button.setText(SResources.getString("b.deleteLogFile"));
      button.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent event) {
            File logFile = new File(VersionInfo.getDownloadLogFile());
            logFile.delete();
            button.setEnabled(false);
         }
      });
      this.createButton(composite, 0, SResources.getString("b.ok"), true);
   }

   protected Control createDialogArea(Composite parent) {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(new FillLayout());
      new DownloadCompleteViewFrame(composite, "l.downloadCompleteTitle", "tab.transfers.buttonSmall", null);
      return composite;
   }

   protected void buttonPressed(int buttonId) {
      super.buttonPressed(buttonId);
   }

   public boolean close() {
      PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "downloadHistoryWindowBounds", this.getShell().getBounds());
      PreferenceLoader.saveStore();
      return super.close();
   }
}
