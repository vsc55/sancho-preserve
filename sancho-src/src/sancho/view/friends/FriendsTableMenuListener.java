package sancho.view.friends;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.File;
import sancho.view.friends.clientDirectories.ClientDirectoriesTableView;
import sancho.view.viewer.GView;
import sancho.view.viewer.table.GTableMenuListenerClient;

public class FriendsTableMenuListener extends GTableMenuListenerClient {
   private ClientDirectoriesTableView clientDirectoriesTableView;
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Client;

   public FriendsTableMenuListener(FriendsTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Client == null
            ? (class$sancho$model$mldonkey$Client = class$("sancho.model.mldonkey.Client"))
            : class$sancho$model$mldonkey$Client
      );
      if (this.selectedObjects.size() > 0) {
         this.clientDirectoriesTableView.setInput(this.selectedObjects.get(0));
      }
   }

   public void setDirectoriesView(ClientDirectoriesTableView var1) {
      this.clientDirectoriesTableView = var1;
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new FriendsTableMenuListener$SendMessageAction(this));
         this.addClientActions(this.gView.getShell(), (File)null, var1, this.createClientArray());
         var1.add(new FriendsTableMenuListener$RemoveFriendAction(this));
      }

      if (this.gView.getItemCount() > 0) {
         var1.add(new FriendsTableMenuListener$RemoveAllFriendsAction(this));
      }

      var1.add(new FriendsTableMenuListener$AddByIPAction(this));
   }

   private void removeSelectedFriends() {
      for (int var1 = 0; var1 < this.selectedObjects.size(); var1++) {
         Client var2 = (Client)this.selectedObjects.get(var1);
         var2.removeAsFriend();
      }
   }

   protected void onDeleteKey() {
      this.removeSelectedFriends();
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   // $VF: synthetic method
   static List access$000(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static void access$200(FriendsTableMenuListener var0) {
      var0.removeSelectedFriends();
   }

   // $VF: synthetic method
   static GView access$300(FriendsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static List access$400(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$500(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$600(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$700(FriendsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static GView access$800(FriendsTableMenuListener var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$900(FriendsTableMenuListener var0) {
      return var0.gView;
   }
}
