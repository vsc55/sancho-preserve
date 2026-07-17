package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class ServerTableMenuListener$RenameAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$RenameAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.rename"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("commit_question"));
   }

   public void run() {
      ServerTableMenuListener.access$700(this.this$0);
   }
}
