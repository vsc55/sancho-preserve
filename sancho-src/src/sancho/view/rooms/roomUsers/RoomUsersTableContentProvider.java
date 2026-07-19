package sancho.view.rooms.roomUsers;

import org.eclipse.jface.viewers.Viewer;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableContentProviderOM;

public class RoomUsersTableContentProvider extends GTableContentProviderOM {
   public static final String S_ROOM_USERS = SResources.getString("t.r.roomUsers");

   public RoomUsersTableContentProvider(RoomUsersTableView view) {
      super(view);
   }

   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
      super.inputChanged(viewer, oldInput, newInput);
      if (oldInput != null) {
         ((MyObservable)oldInput).deleteObserver(this);
      }

      if (newInput != null) {
         ((ObjectMap)newInput).addObserver(this);
         this.updateHeaderLabel(((ObjectMap)newInput).size());
      }
   }

   public void updateHeaderLabel(int count) {
      this.gView.getViewFrame().updateCLabelText(S_ROOM_USERS + ": " + " " + count);
   }
}
