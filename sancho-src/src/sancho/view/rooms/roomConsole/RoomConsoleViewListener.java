package sancho.view.rooms.roomConsole;

import org.eclipse.jface.action.IMenuManager;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;

class RoomConsoleViewListener extends SashViewListener {
   public RoomConsoleViewListener(SashViewFrame viewFrame) {
      super(viewFrame);
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      this.createSashActions(menuManager, "t.r.roomUsers");
   }
}
