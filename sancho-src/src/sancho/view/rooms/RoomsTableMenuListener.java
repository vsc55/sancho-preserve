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

   public RoomsTableMenuListener(RoomsTableView view) {
      super(view);
   }

   public void selectionChanged(SelectionChangedEvent event) {
      this.collectSelections(
         event,
         class$sancho$model$mldonkey$Room == null
            ? (class$sancho$model$mldonkey$Room = class$("sancho.model.mldonkey.Room"))
            : class$sancho$model$mldonkey$Room
      );
   }

   public void menuAboutToShow(IMenuManager menuManager) {
      if (this.selectedObjects.size() > 0) {
         menuManager.add(new SetRoomStateAction(EnumRoomState.OPEN));
         menuManager.add(new SetRoomStateAction(EnumRoomState.CLOSED));
         menuManager.add(new SetRoomStateAction(EnumRoomState.PAUSED));
         this.addSelectAllMenu(menuManager);
      }
   }

   // $VF: synthetic method
   static Class class$(String className) {
      try {
         return Class.forName(className);
      } catch (ClassNotFoundException exception) {
         throw new NoClassDefFoundError(exception.getMessage());
      }
   }

   // Menu action that applies a room state to the currently selected rooms.
   private class SetRoomStateAction extends Action {
      private EnumRoomState enumRoomState;

      public SetRoomStateAction(EnumRoomState enumRoomState) {
         super("Set: " + enumRoomState.getName());
         this.enumRoomState = enumRoomState;
         this.setImageDescriptor(SResources.getImageDescriptor("rooms.buttonActiveSmall"));
      }

      public void run() {
         for (int i = 0; i < selectedObjects.size(); i++) {
            Room room = (Room)selectedObjects.get(i);
            room.setRoomState(this.enumRoomState);
         }
      }
   }
}
