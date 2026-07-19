package sancho.view.friends;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.core.Sancho;
import sancho.model.mldonkey.Client;
import sancho.model.mldonkey.ClientCollection;
import sancho.model.mldonkey.File;
import sancho.view.FriendsTab;
import sancho.view.friends.clientDirectories.ClientDirectoriesTableView;
import sancho.view.utility.SResources;
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
         var1.add(new SendMessageAction());
         this.addClientActions(this.gView.getShell(), (File)null, var1, this.createClientArray());
         var1.add(new RemoveFriendAction());
      }

      if (this.gView.getItemCount() > 0) {
         var1.add(new RemoveAllFriendsAction());
      }

      var1.add(new AddByIPAction());
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

   // Menu action: add a friend by IP address.
   private class AddByIPAction extends Action {
      public AddByIPAction() {
         super(SResources.getString("mi.f.addByIP"));
         this.setImageDescriptor(SResources.getImageDescriptor("tab.friends.buttonSmall"));
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            new AddFriendDialog(gView.getShell()).open();
         }
      }
   }

   // Menu action: remove every friend from the list.
   private class RemoveAllFriendsAction extends Action {
      public RemoveAllFriendsAction() {
         super(SResources.getString("mi.f.removeAllFriends"));
         this.setImageDescriptor(SResources.getImageDescriptor("FriendsButtonSmallBW"));
      }

      public void run() {
         if (Sancho.hasCollectionFactory()) {
            ClientCollection.removeAllFriends(gView.getCore());
         }
      }
   }

   // Menu action: remove the selected friend(s).
   private class RemoveFriendAction extends Action {
      public RemoveFriendAction() {
         String var2 = selectedObjects.size() > 1 ? " (" + selectedObjects.size() + ")" : "";
         this.setText(SResources.getString("mi.f.removeFriend") + var2);
         this.setImageDescriptor(SResources.getImageDescriptor("FriendsButtonSmallBW"));
      }

      public void run() {
         removeSelectedFriends();
      }
   }

   // Menu action: open a message tab for the selected friend(s).
   private class SendMessageAction extends Action {
      public SendMessageAction() {
         String var2 = selectedObjects.size() > 1 ? " (" + selectedObjects.size() + ")" : "";
         this.setText(SResources.getString("mi.f.sendMessage") + var2);
         this.setImageDescriptor(SResources.getImageDescriptor("resume"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            Client var2 = (Client)selectedObjects.get(var1);
            FriendsTab var3 = (FriendsTab)gView.getViewFrame().getATab();
            var3.openTab(var2);
         }
      }
   }
}
