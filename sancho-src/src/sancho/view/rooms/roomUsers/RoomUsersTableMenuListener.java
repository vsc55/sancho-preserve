package sancho.view.rooms.roomUsers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.User;
import sancho.view.viewer.actions.AddUserAsFriendAction;
import sancho.view.viewer.table.GTableMenuListener;

public class RoomUsersTableMenuListener extends GTableMenuListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$User;

   public RoomUsersTableMenuListener(RoomUsersTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$User == null
            ? (class$sancho$model$mldonkey$User = class$("sancho.model.mldonkey.User"))
            : class$sancho$model$mldonkey$User
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         User[] users = new User[this.selectedObjects.size()];

         for (int i = 0; i < this.selectedObjects.size(); i++) {
            users[i] = (User)this.selectedObjects.get(i);
         }

         menuManager.add(new AddUserAsFriendAction(users));
      }
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }
}
