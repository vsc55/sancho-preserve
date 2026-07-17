package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class ServerTableMenuListener$RemoveServerAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$RemoveServerAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.removeServer"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("minus"));
   }

   public void run() {
      ServerTableMenuListener.access$1400(this.this$0);
   }
}
