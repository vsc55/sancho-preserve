package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Server;
import sancho.view.utility.SResources;

class ServerTableMenuListener$BlackListAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$BlackListAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.blacklist"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("gun"));
   }

   public void run() {
      for (int var1 = 0; var1 < ServerTableMenuListener.access$000(this.this$0).size(); var1++) {
         ((Server)ServerTableMenuListener.access$100(this.this$0).get(var1)).blacklist();
      }
   }
}
