package sancho.view;

import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;

class RoomsTab$1 extends CTabFolder2Adapter {
   // $VF: synthetic field
   private final RoomsTab this$0;

   RoomsTab$1(RoomsTab var1) {
      this.this$0 = var1;
   }

   public void close(CTabFolderEvent var1) {
      this.this$0.closeTab((CTabItem)var1.item);
   }
}
