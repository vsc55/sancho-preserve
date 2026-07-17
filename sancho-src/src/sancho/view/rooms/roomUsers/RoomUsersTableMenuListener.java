package sancho.view.rooms.roomUsers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.User;
import sancho.view.viewer.actions.AddUserAsFriendAction;
import sancho.view.viewer.table.GTableMenuListener;

public class RoomUsersTableMenuListener extends GTableMenuListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$User;

   public RoomUsersTableMenuListener(RoomUsersTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$User == null
            ? (class$sancho$model$mldonkey$User = class$("sancho.model.mldonkey.User"))
            : class$sancho$model$mldonkey$User
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         User[] var2 = new User[this.selectedObjects.size()];

         for (int var3 = 0; var3 < this.selectedObjects.size(); var3++) {
            var2[var3] = (User)this.selectedObjects.get(var3);
         }

         var1.add(new AddUserAsFriendAction(var2));
      }
   }

   // $VF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
