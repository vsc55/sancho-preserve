package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Server;
import sancho.view.utility.SResources;

class ServerTableMenuListener$PreferredServerAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$PreferredServerAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.preferredServer"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("heart"));
   }

   public void run() {
      for (int var1 = 0; var1 < ServerTableMenuListener.access$1200(this.this$0).size(); var1++) {
         ((Server)ServerTableMenuListener.access$1300(this.this$0).get(var1)).togglePreferred();
      }
   }
}
