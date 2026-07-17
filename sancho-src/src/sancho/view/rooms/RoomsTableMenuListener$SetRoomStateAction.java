package sancho.view.rooms;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Room;
import sancho.model.mldonkey.enums.EnumRoomState;
import sancho.view.utility.SResources;

class RoomsTableMenuListener$SetRoomStateAction extends Action {
   private EnumRoomState enumRoomState;
   // $VF: synthetic field
   private final RoomsTableMenuListener this$0;

   public RoomsTableMenuListener$SetRoomStateAction(RoomsTableMenuListener var1, EnumRoomState var2) {
      super("Set: " + var2.getName());
      this.this$0 = var1;
      this.enumRoomState = var2;
      this.setImageDescriptor(SResources.getImageDescriptor("rooms.buttonActiveSmall"));
   }

   public void run() {
      for (int var1 = 0; var1 < RoomsTableMenuListener.access$000(this.this$0).size(); var1++) {
         Room var2 = (Room)RoomsTableMenuListener.access$100(this.this$0).get(var1);
         var2.setRoomState(this.enumRoomState);
      }
   }
}
