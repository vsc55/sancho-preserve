package sancho.view.friends;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.FriendsTab;
import sancho.view.utility.SResources;

class FriendsTableMenuListener$SendMessageAction extends Action {
   // $VF: synthetic field
   private final FriendsTableMenuListener this$0;

   public FriendsTableMenuListener$SendMessageAction(FriendsTableMenuListener var1) {
      this.this$0 = var1;
      String var2 = FriendsTableMenuListener.access$400(var1).size() > 1 ? " (" + FriendsTableMenuListener.access$500(var1).size() + ")" : "";
      this.setText(SResources.getString("mi.f.sendMessage") + var2);
      this.setImageDescriptor(SResources.getImageDescriptor("resume"));
   }

   public void run() {
      for (int var1 = 0; var1 < FriendsTableMenuListener.access$600(this.this$0).size(); var1++) {
         Client var2 = (Client)FriendsTableMenuListener.access$700(this.this$0).get(var1);
         FriendsTab var3 = (FriendsTab)FriendsTableMenuListener.access$800(this.this$0).getViewFrame().getATab();
         var3.openTab(var2);
      }
   }
}
