package sancho.view;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

class RoomsTab$RoomCTabFolderViewListener extends SashViewListener {
   public RoomsTab$RoomCTabFolderViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createSashActions(var1, "t.r.availableRooms");
   }
}
