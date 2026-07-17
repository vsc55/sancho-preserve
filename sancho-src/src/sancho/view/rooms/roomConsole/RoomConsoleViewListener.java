package sancho.view.rooms.roomConsole;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

class RoomConsoleViewListener extends SashViewListener {
   public RoomConsoleViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      this.createSashActions(var1, "t.r.roomUsers");
   }
}
