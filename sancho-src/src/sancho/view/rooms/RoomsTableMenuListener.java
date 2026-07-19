package sancho.view.rooms;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.enums.EnumRoomState;
import sancho.view.utility.SResources;
import sancho.view.viewer.table.GTableMenuListener;

public class RoomsTableMenuListener extends GTableMenuListener {
   // $VF: synthetic field
   static Class class$sancho$model$mldonkey$Room;

   public RoomsTableMenuListener(RoomsTableView var1) {
      super(var1);
   }

   public void selectionChanged(SelectionChangedEvent var1) {
      this.collectSelections(
         var1,
         class$sancho$model$mldonkey$Room == null
            ? (class$sancho$model$mldonkey$Room = class$("sancho.model.mldonkey.Room"))
            : class$sancho$model$mldonkey$Room
      );
   }

   public void menuAboutToShow(IMenuManager var1) {
      if (this.selectedObjects.size() > 0) {
         var1.add(new SetRoomStateAction(EnumRoomState.OPEN));
         var1.add(new SetRoomStateAction(EnumRoomState.CLOSED));
         var1.add(new SetRoomStateAction(EnumRoomState.PAUSED));
         this.addSelectAllMenu(var1);
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

   // Menu action that applies a room state to the currently selected rooms.
   private class SetRoomStateAction extends Action {
      private EnumRoomState enumRoomState;

      public SetRoomStateAction(EnumRoomState var2) {
         super("Set: " + var2.getName());
         this.enumRoomState = var2;
         this.setImageDescriptor(SResources.getImageDescriptor("rooms.buttonActiveSmall"));
      }

      public void run() {
         for (int var1 = 0; var1 < selectedObjects.size(); var1++) {
            Room var2 = (Room)selectedObjects.get(var1);
            var2.setRoomState(this.enumRoomState);
         }
      }
   }
}
