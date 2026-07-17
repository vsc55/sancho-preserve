package sancho.view.rooms;

import org.eclipse.swt.graphics.Image;
import sancho.model.mldonkey.Room;
import sancho.view.viewer.table.GTableLabelProvider;

public class RoomsTableLabelProvider extends GTableLabelProvider {
   public RoomsTableLabelProvider(RoomsTableView var1) {
      super(var1);
   }

   public Image getColumnImage(Object var1, int var2) {
      Room var3 = (Room)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
         case 3:
            return var3.getNetworkImage();
         default:
            return null;
      }
   }

   public String getColumnText(Object var1, int var2) {
      Room var3 = (Room)var1;
      switch (this.cViewer.getColumnIDs()[var2]) {
         case 0:
            return var3.getName();
         case 1:
            return String.valueOf(var3.getNumUsers()).intern();
         case 2:
            return var3.getRoomState().getName();
         case 3:
            return var3.getNetworkName();
         case 4:
            return String.valueOf(var3.getId()).intern();
         default:
            return "?";
      }
   }
}
