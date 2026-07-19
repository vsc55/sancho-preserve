package sancho.view.shares;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import sancho.model.mldonkey.SharedFile;
import sancho.utility.VersionInfo;
import sancho.view.transfer.fileDialog.SubfilesViewFrame;
import sancho.view.utility.SResources;

public class SharedFileDetailDialog extends Dialog {
   SharedFile sharedFile;

   protected SharedFileDetailDialog(Shell shell, SharedFile sharedFile) {
      super(shell);
      this.sharedFile = sharedFile;
   }

   protected void constrainShellSize() {
      super.constrainShellSize();
      Shell shell = this.getShell();
      shell.setSize(400, 350);
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
      shell.setText(SResources.getString("l.file") + " " + this.sharedFile.getId() + " " + SResources.getString("l.details").toLowerCase());
   }

   protected void createButtonsForButtonBar(Composite composite) {
      this.createButton(composite, 0, SResources.getString("b.ok"), true);
   }

   protected Control createDialogArea(Composite parent) {
      Composite dialogArea = (Composite)super.createDialogArea(parent);
      Composite composite = new Composite(dialogArea, 0);
      composite.setLayout(new FillLayout());
      composite.setLayoutData(new GridData(1808));
      new SubfilesViewFrame(composite, "l.subFiles", "up_arrow_blue", null, this.sharedFile);
      return dialogArea;
   }
}
