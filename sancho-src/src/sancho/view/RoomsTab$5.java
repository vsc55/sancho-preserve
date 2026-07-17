package sancho.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class RoomsTab$5 extends SelectionAdapter {
   // $VF: synthetic field
   private final RoomsTab$RoomCTabFolderViewFrame this$1;

   RoomsTab$5(RoomsTab$RoomCTabFolderViewFrame var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      RoomsTab$RoomCTabFolderViewFrame.access$000(this.this$1).closeAllTabs();
   }
}
