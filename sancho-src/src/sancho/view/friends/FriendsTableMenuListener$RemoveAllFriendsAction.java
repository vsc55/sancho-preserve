package sancho.view.friends;

import org.eclipse.jface.action.Action;
import sancho.core.Sancho;
import sancho.model.mldonkey.ClientCollection;
import sancho.view.utility.SResources;

class FriendsTableMenuListener$RemoveAllFriendsAction extends Action {
   // $VF: synthetic field
   private final FriendsTableMenuListener this$0;

   public FriendsTableMenuListener$RemoveAllFriendsAction(FriendsTableMenuListener var1) {
      super(SResources.getString("mi.f.removeAllFriends"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("FriendsButtonSmallBW"));
   }

   public void run() {
      if (Sancho.hasCollectionFactory()) {
         ClientCollection.removeAllFriends(FriendsTableMenuListener.access$300(this.this$0).getCore());
      }
   }
}
