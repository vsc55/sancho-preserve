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
   public DownloadCompleteShell(Shell var1) {
      super(var1);
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
      var1.setText(SResources.getString("l.downloadCompleteTitle"));
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      this.getShell().setBounds(PreferenceLoader.loadRectangle("downloadHistoryWindowBounds"));
   }

   public int getShellStyle() {
      return super.getShellStyle() | 16;
   }

   protected void createButtonsForButtonBar(Composite var1) {
      ((GridLayout)var1.getLayout()).numColumns++;
      final Button var2 = new Button(var1, 0);
      var2.setText(SResources.getString("b.deleteLogFile"));
      var2.addSelectionListener(new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var3) {
            File logFile = new File(VersionInfo.getDownloadLogFile());
            logFile.delete();
            var2.setEnabled(false);
         }
      });
      this.createButton(var1, 0, SResources.getString("b.ok"), true);
   }

   protected Control createDialogArea(Composite var1) {
      Composite var2 = (Composite)super.createDialogArea(var1);
      var2.setLayout(new FillLayout());
      new DownloadCompleteViewFrame(var2, "l.downloadCompleteTitle", "tab.transfers.buttonSmall", null);
      return var2;
   }

   protected void buttonPressed(int var1) {
      super.buttonPressed(var1);
   }

   public boolean close() {
      PreferenceConverter.setValue(PreferenceLoader.getPreferenceStore(), "downloadHistoryWindowBounds", this.getShell().getBounds());
      PreferenceLoader.saveStore();
      return super.close();
   }
}
