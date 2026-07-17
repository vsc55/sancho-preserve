package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.User;
import sancho.view.utility.SResources;

public class AddUserAsFriendAction extends Action {
   private User[] userArray;

   public AddUserAsFriendAction(User[] var1) {
      super(SResources.getString("dd.c.addFriend"));
      this.setImageDescriptor(SResources.getImageDescriptor("tab.friends.buttonSmall"));
      this.userArray = var1;
   }

   public void run() {
      for (int var1 = 0; var1 < this.userArray.length; var1++) {
         this.userArray[var1].addAsFriend();
      }
   }
}
