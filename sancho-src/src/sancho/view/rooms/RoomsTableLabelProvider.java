package sancho.view.rooms;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Room;
import sancho.view.viewer.table.GTableLabelProvider;

public class RoomsTableLabelProvider extends GTableLabelProvider {
   public RoomsTableLabelProvider(RoomsTableView view) {
      super(view);
   }

   public Image getColumnImage(Object element, int columnIndex) {
      Room room = (Room)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
         case 3:
            return room.getNetworkImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object element, int columnIndex) {
      Room room = (Room)element;
      switch (this.cViewer.getColumnIDs()[columnIndex]) {
         case 0:
            return room.getName();
         case 1:
            return String.valueOf(room.getNumUsers()).intern();
         case 2:
            return room.getRoomState().getName();
         case 3:
            return room.getNetworkName();
         case 4:
            return String.valueOf(room.getId()).intern();
         default:
            return "?";
      }
   }
}
