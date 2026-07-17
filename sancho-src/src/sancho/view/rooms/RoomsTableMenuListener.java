package sancho.view.rooms;

import java.util.List;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import sancho.model.mldonkey.enums.EnumRoomState;
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
         var1.add(new RoomsTableMenuListener$SetRoomStateAction(this, EnumRoomState.OPEN));
         var1.add(new RoomsTableMenuListener$SetRoomStateAction(this, EnumRoomState.CLOSED));
         var1.add(new RoomsTableMenuListener$SetRoomStateAction(this, EnumRoomState.PAUSED));
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

   // $VF: synthetic method
   static List access$000(RoomsTableMenuListener var0) {
      return var0.selectedObjects;
   }

   // $VF: synthetic method
   static List access$100(RoomsTableMenuListener var0) {
      return var0.selectedObjects;
   }
}
