package sancho.view.rooms.roomUsers;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewFrame.SashViewListener;
import sancho.view.viewer.actions.ColumnSelectorAction;

public class RoomUsersViewListener extends SashViewListener {
   public RoomUsersViewListener(SashViewFrame var1) {
      super(var1);
   }

   public void menuAboutToShow(IMenuManager var1) {
      var1.add(new ColumnSelectorAction(this.gView));
      var1.add(new Separator());
      this.createDynamicColumnSubMenu(var1);
      this.createSortByColumnSubMenu(var1);
      this.createSashActions(var1, "t.r.roomConsole");
   }
}
