package sancho.view.rooms;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Room;
import sancho.view.viewer.GSorter;

public class RoomsTableSorter extends GSorter {
   public RoomsTableSorter(RoomsTableView var1) {
      super(var1);
   }

   protected int _compare(Viewer var1, Object var2, Object var3, int var4) {
      Room var5 = (Room)var2;
      Room var6 = (Room)var3;
      switch (var4) {
         case 0:
            return this.compareStrings(var5.getName(), var6.getName());
         case 1:
            return this.compareInts(var5.getNumUsers(), var6.getNumUsers());
         case 2:
            return this.compareStrings(var5.getRoomState().getName(), var6.getRoomState().getName());
         case 3:
            return this.compareStrings(var5.getNetworkName(), var6.getNetworkName());
         case 4:
            return this.compareInts(var5.getId(), var6.getId());
         default:
            return 0;
      }
   }
}
