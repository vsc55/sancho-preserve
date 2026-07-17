package sancho.view.utility.setupWizard;

import org.eclipse.jface.wizard.Wizard;
import sancho.view.preferences.PreferenceLoader;
import sancho.view.utility.SResources;

public class SetupWizard extends Wizard {
   private HostPage hostPage;
   private CoreBinaryPage coreBinaryPage;

   public SetupWizard() {
      this.setWindowTitle(SResources.getString("hm.setupTitle"));
      this.hostPage = new SSHHostPage();
      this.coreBinaryPage = new CoreBinaryPage();
   }

   public boolean performFinish() {
      this.hostPage.saveData();
      this.coreBinaryPage.saveData();
      ((SetupWizardDialog)this.getContainer()).setNum(this.hostPage.getNum());
      return true;
   }

   public void addPages() {
      if (!PreferenceLoader.loadBoolean("initialized")) {
         this.addPage(new WelcomePage());
         this.addPage(this.coreBinaryPage);
      }

      this.addPage(this.hostPage);
   }
}
