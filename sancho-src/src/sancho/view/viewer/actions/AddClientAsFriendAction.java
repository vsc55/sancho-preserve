package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class AddClientAsFriendAction extends Action {
   private Client[] clientArray;

   public AddClientAsFriendAction(Client[] var1) {
      super(SResources.getString("dd.c.addFriend"));
      this.setImageDescriptor(SResources.getImageDescriptor("tab.friends.buttonSmall"));
      this.clientArray = var1;
   }

   public void run() {
      for (int var1 = 0; var1 < this.clientArray.length; var1++) {
         this.clientArray[var1].addAsFriend();
      }
   }
}
