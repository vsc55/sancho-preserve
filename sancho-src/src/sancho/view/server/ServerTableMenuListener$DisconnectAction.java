package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Server;
import sancho.view.utility.SResources;

class ServerTableMenuListener$DisconnectAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$DisconnectAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.disconnect"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("menu-disconnect"));
   }

   public void run() {
      for (int var1 = 0; var1 < ServerTableMenuListener.access$800(this.this$0).size(); var1++) {
         ((Server)ServerTableMenuListener.access$900(this.this$0).get(var1)).disconnect();
      }
   }
}
