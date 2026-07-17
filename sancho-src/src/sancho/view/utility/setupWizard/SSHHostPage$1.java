package sancho.view.utility.setupWizard;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class SSHHostPage$1 extends SelectionAdapter {
   // $VF: synthetic field
   private final SSHHostPage this$0;

   SSHHostPage$1(SSHHostPage var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      this.this$0.toggleSSHEnabled(this.this$0.use_ssh.getSelection());
   }
}
