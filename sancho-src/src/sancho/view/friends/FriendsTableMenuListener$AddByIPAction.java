package sancho.view.friends;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.view.utility.SResources;

class FriendsTableMenuListener$AddByIPAction extends Action {
   // $VF: synthetic field
   private final FriendsTableMenuListener this$0;

   public FriendsTableMenuListener$AddByIPAction(FriendsTableMenuListener var1) {
      super(SResources.getString("mi.f.addByIP"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("tab.friends.buttonSmall"));
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         new AddFriendDialog(FriendsTableMenuListener.access$900(this.this$0).getShell()).open();
      }
   }
}
