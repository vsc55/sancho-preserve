package sancho.view.rooms;

import org.eclipse.swt.widgets.Composite;
import sancho.core.Sancho;
import sancho.model.mldonkey.enums.AbstractEnum;
import sancho.model.mldonkey.enums.EnumRoomState;
import sancho.view.viewFrame.ViewFrame;
import sancho.view.viewer.table.GTableView;

public class RoomsTableView extends GTableView {
   public static final int NAME = 0;
   public static final int USERS = 1;
   public static final int STATE = 2;
   public static final int NETWORK = 3;
   public static final int NUMBER = 4;

   public RoomsTableView(ViewFrame viewFrame) {
      super(viewFrame);
      this.preferenceString = "rooms";
      this.columnLabels = new String[]{"rooms.name", "rooms.users", "rooms.state", "rooms.network", "rooms.number"};
      this.columnAlignment = new int[]{16384, 131072, 16384, 16384, 131072};
      this.columnDefaultWidths = new int[]{150, 50, 75, 150, 50};
      this.validStates = new AbstractEnum[]{EnumRoomState.OPEN, EnumRoomState.CLOSED, EnumRoomState.PAUSED};
      this.gSorter = new RoomsTableSorter(this);
      this.tableContentProvider = new RoomsTableContentProvider(this);
      this.tableLabelProvider = new RoomsTableLabelProvider(this);
      this.tableMenuListener = new RoomsTableMenuListener(this);
      this.createContents(viewFrame.getChildComposite());
   }

   protected void createContents(Composite composite) {
      super.createContents(composite);
      this.sViewer.addSelectionChangedListener((RoomsTableMenuListener)this.tableMenuListener);
      this.addMenuListener();
   }

   public void setInput() {
      if (Sancho.hasCollectionFactory()) {
         this.sViewer.setInput(this.getCore().getRoomCollection());
      }
   }
}
