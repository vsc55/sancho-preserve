package sancho.view.friends;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class FriendsTableMenuListener$RemoveFriendAction extends Action {
   // $VF: synthetic field
   private final FriendsTableMenuListener this$0;

   public FriendsTableMenuListener$RemoveFriendAction(FriendsTableMenuListener var1) {
      this.this$0 = var1;
      String var2 = FriendsTableMenuListener.access$000(var1).size() > 1 ? " (" + FriendsTableMenuListener.access$100(var1).size() + ")" : "";
      this.setText(SResources.getString("mi.f.removeFriend") + var2);
      this.setImageDescriptor(SResources.getImageDescriptor("FriendsButtonSmallBW"));
   }

   public void run() {
      FriendsTableMenuListener.access$200(this.this$0);
   }
}
