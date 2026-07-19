package sancho.view.rooms.roomUsers;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.Room;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class RoomUsersTableView extends GTableView {
   public static final int NAME = 0;
   public static final int TAGS = 1;
   public static final int ADDR = 2;
   public static final int PORT = 3;
   public static final int SERVER = 4;
   public Room room;

   public RoomUsersTableView(ViewFrame viewFrame, Room room) {
      super(viewFrame);
      this.room = room;
      this.preferenceString = "roomUsers";
      this.columnLabels = new String[]{"roomUsers.name", "roomUsers.tags", "roomUsers.addr", "roomUsers.port", "roomUsers.server"};
      this.columnAlignment = new int[]{16384, 16384, 131072, 131072, 131072};
      this.columnDefaultWidths = new int[]{150, 150, 100, 50, 50};
      this.gSorter = new RoomUsersTableSorter(this);
      this.tableContentProvider = new RoomUsersTableContentProvider(this);
      this.tableLabelProvider = new RoomUsersTableLabelProvider(this);
      this.tableMenuListener = new RoomUsersTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((RoomUsersTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory() && this.room != null) {
         this.sViewer.setInput(this.room.getUserMap());
      }
   }
}
