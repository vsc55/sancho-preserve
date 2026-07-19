package sancho.view.rooms.roomUsers;

import org.eclipse.swt.custom.SashForm;
import sancho.model.mldonkey.Room;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class RoomUsersViewFrame extends SashViewFrame {
   public RoomUsersViewFrame(SashForm sashForm, String name, String text, AbstractTab tab, Room room) {
      super(sashForm, name, text, tab, true);
      this.gView = new RoomUsersTableView(this, room);
      this.createViewListener(new RoomUsersViewListener(this));
   }
}
