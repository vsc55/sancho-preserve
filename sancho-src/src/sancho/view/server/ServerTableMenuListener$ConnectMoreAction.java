package sancho.view.server;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class ServerTableMenuListener$ConnectMoreAction extends Action {
   // $VF: synthetic field
   private final ServerTableMenuListener this$0;

   public ServerTableMenuListener$ConnectMoreAction(ServerTableMenuListener var1) {
      super(SResources.getString("m.srv.connectMore"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("plus"));
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         ServerTableMenuListener.access$400(this.this$0).getCore().getServerCollection().connectMore();
      }
   }
}
