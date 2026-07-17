package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.Server;
import sancho.view.utility.SResources;

class ServerTableMenuListener$ConnectAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$ConnectAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.connect"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("menu-connect"));
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         for (int var1 = 0; var1 < ServerTableMenuListener.access$200(this.this$0).size(); var1++) {
            ((Server)ServerTableMenuListener.access$300(this.this$0).get(var1)).connect();
         }
      }
   }
}
