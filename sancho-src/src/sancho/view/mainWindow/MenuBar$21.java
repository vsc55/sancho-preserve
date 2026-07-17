package sancho.view.mainWindow;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import sancho.view.utility.setupWizard.SetupWizard;
import sancho.view.utility.setupWizard.SetupWizardDialog;

class MenuBar$21 implements Listener {
   // $VF: synthetic field
   private final MenuBar this$0;

   MenuBar$21(MenuBar var1) {
      this.this$0 = var1;
   }

   public void handleEvent(Event var1) {
      SetupWizardDialog var2 = new SetupWizardDialog(MenuBar.access$100(this.this$0).getShell(), new SetupWizard());
      var2.create();
      var2.open();
   }
}
