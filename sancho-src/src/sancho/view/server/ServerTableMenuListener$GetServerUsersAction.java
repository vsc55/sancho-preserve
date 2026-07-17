package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Server;
import sancho.view.utility.SResources;

class ServerTableMenuListener$GetServerUsersAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$GetServerUsersAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.getServerUsers"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("rotate"));
   }

   public void run() {
      for (int var1 = 0; var1 < ServerTableMenuListener.access$1000(this.this$0).size(); var1++) {
         ((Server)ServerTableMenuListener.access$1100(this.this$0).get(var1)).getServerUsers();
      }
   }
}
