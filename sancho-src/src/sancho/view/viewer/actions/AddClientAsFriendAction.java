package sancho.view.viewer.actions;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Client;
import sancho.view.utility.SResources;

public class AddClientAsFriendAction extends Action {
   private Client[] clientArray;

   public AddClientAsFriendAction(Client[] clientArray) {
      super(SResources.getString("dd.c.addFriend"));
      this.setImageDescriptor(SResources.getImageDescriptor("tab.friends.buttonSmall"));
      this.clientArray = clientArray;
   }

   public void run() {
      for (int i = 0; i < this.clientArray.length; i++) {
         this.clientArray[i].addAsFriend();
      }
   }
}
