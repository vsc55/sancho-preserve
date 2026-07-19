package sancho.view.rooms;

import org.eclipse.jface.viewers.Viewer;
import sancho.model.mldonkey.Room;
import sancho.view.viewer.GSorter;

public class RoomsTableSorter extends GSorter {
   public RoomsTableSorter(RoomsTableView view) {
      super(view);
   }

   protected int _compare(Viewer viewer, Object element1, Object element2, int columnId) {
      Room room1 = (Room)element1;
      Room room2 = (Room)element2;
      switch (columnId) {
         case 0:
            return this.compareStrings(room1.getName(), room2.getName());
         case 1:
            return this.compareInts(room1.getNumUsers(), room2.getNumUsers());
         case 2:
            return this.compareStrings(room1.getRoomState().getName(), room2.getRoomState().getName());
         case 3:
            return this.compareStrings(room1.getNetworkName(), room2.getNetworkName());
         case 4:
            return this.compareInts(room1.getId(), room2.getId());
         default:
            return 0;
      }
   }
}
