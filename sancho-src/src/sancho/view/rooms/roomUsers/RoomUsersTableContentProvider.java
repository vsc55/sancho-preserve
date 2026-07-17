package sancho.view.rooms.roomUsers;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class RoomUsersTableContentProvider extends GTableContentProviderOM {
   public static final String S_ROOM_USERS = SResources.getString("t.r.roomUsers");

   public RoomUsersTableContentProvider(RoomUsersTableView var1) {
      super(var1);
   }

   public void inputChanged(Viewer var1, Object var2, Object var3) {
      super.inputChanged(var1, var2, var3);
      if (var2 != null) {
         ((MyObservable)var2).deleteObserver(this);
      }

      if (var3 != null) {
         ((ObjectMap)var3).addObserver(this);
         this.updateHeaderLabel(((ObjectMap)var3).size());
      }
   }

   public void updateHeaderLabel(int var1) {
      this.gView.getViewFrame().updateCLabelText(S_ROOM_USERS + ": " + " " + var1);
   }
}
