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

   public SetupWizardDialog(Shell var1, IWizard var2) {
      super(var1, var2);
   }

   protected void createButtonsForButtonBar(Composite var1) {
      super.createButtonsForButtonBar(var1);
      if (!Sancho.hasLoaded()) {
         Button var2 = this.getButton(16);
         var2.setText(SResources.getString("b.connect"));
         Button var3 = this.getButton(1);
         var3.setText(SResources.getString("b.quit"));
      }
   }

   public void setNum(int var1) {
      this.hm_num = var1;
   }

   public int getNum() {
      return this.hm_num;
   }

   protected void configureShell(Shell var1) {
      super.configureShell(var1);
      var1.setImage(VersionInfo.getProgramIcon());
   }
}
