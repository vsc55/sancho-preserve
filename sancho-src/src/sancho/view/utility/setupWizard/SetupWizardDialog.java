package sancho.view.utility.setupWizard;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import sancho.core.Sancho;
import sancho.utility.VersionInfo;
import sancho.view.utility.SResources;

public class SetupWizardDialog extends WizardDialog {
   int hm_num;

   public SetupWizardDialog(Shell shell, IWizard wizard) {
      super(shell, wizard);
   }

   protected void createButtonsForButtonBar(Composite parent) {
      super.createButtonsForButtonBar(parent);
      if (!Sancho.hasLoaded()) {
         Button connectButton = this.getButton(16);
         connectButton.setText(SResources.getString("b.connect"));
         Button quitButton = this.getButton(1);
         quitButton.setText(SResources.getString("b.quit"));
      }
   }

   public void setNum(int num) {
      this.hm_num = num;
   }

   public int getNum() {
      return this.hm_num;
   }

   protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setImage(VersionInfo.getProgramIcon());
   }
}
