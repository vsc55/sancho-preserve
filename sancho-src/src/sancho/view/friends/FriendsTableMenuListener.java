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

   public FriendsTableMenuListener(FriendsTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Client == null
            ? (class$sancho$model$mldonkey$Client = class$("sancho.model.mldonkey.Client"))
            : class$sancho$model$mldonkey$Client
      );
      if (this.selectedObjects.size() > 0) {
         this.clientDirectoriesTableView.setInput(this.selectedObjects.get(0));
      }
   }

   public void setDirectoriesView(ClientDirectoriesTableView view) {
      this.clientDirectoriesTableView = view;
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new SendMessageAction());
         this.addClientActions(this.gView.getShell(), (File)null, menuManager, this.createClientArray());
         menuManager.add(new RemoveFriendAction());
      }

      if (this.gView.getItemCount() > 0) {
         menuManager.add(new RemoveAllFriendsAction());
      }

      menuManager.add(new AddByIPAction());
   }

   private void removeSelectedFriends() {
      for (int i = 0; i < this.selectedObjects.size(); i++) {
         Client client = (Client)this.selectedObjects.get(i);
         client.removeAsFriend();
      }
   }

   protected void onDeleteKey() {
      this.removeSelectedFriends();
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
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
         String text = selectedObjects.size() > 1 ? " (" + selectedObjects.size() + ")" : "";
         this.setText(SResources.getString("mi.f.removeFriend") + text);
         this.setImageDescriptor(SResources.getImageDescriptor("FriendsButtonSmallBW"));
      }

      public void run() {
         removeSelectedFriends();
      }
   }

   // Menu action: open a message tab for the selected friend(s).
   private class SendMessageAction extends Action {
      public SendMessageAction() {
         String text = selectedObjects.size() > 1 ? " (" + selectedObjects.size() + ")" : "";
         this.setText(SResources.getString("mi.f.sendMessage") + text);
         this.setImageDescriptor(SResources.getImageDescriptor("resume"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            Client client = (Client)selectedObjects.get(i);
            FriendsTab tab = (FriendsTab)gView.getViewFrame().getATab();
            tab.openTab(client);
         }
      }
   }
}
