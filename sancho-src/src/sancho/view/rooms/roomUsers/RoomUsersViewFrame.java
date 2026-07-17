package sancho.view.rooms.roomUsers;

import org.eclipse.swt.custom.SashForm;
import sancho.model.mldonkey.Room;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class RoomUsersViewFrame extends SashViewFrame {
   public RoomUsersViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, Room var5) {
      super(var1, var2, var3, var4, true);
      this.gView = new RoomUsersTableView(this, var5);
      this.createViewListener(new RoomUsersViewListener(this));
   }
}
