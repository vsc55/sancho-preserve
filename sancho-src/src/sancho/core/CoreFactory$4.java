package sancho.core;

import sancho.view.utility.Splash;
import sancho.view.utility.setupWizard.SetupWizard;
import sancho.view.utility.setupWizard.SetupWizardDialog;

class CoreFactory$4 implements Runnable {
   // $VF: synthetic field
   private final CoreFactory this$0;

   CoreFactory$4(CoreFactory var1) {
      this.this$0 = var1;
   }

   public void run() {
      Splash.setVisible(false);
      SetupWizardDialog var1 = new SetupWizardDialog(null, new SetupWizard());
      var1.create();
      CoreFactory.access$002(this.this$0, 0 == var1.open());
      CoreFactory.access$102(this.this$0, var1.getNum());
      Splash.setVisible(true);
   }
}
