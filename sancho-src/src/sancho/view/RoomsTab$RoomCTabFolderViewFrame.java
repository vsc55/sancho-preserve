package sancho.view;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

class RoomsTab$RoomCTabFolderViewFrame extends SashViewFrame {
   // $VF: synthetic field
   private final RoomsTab this$0;

   public RoomsTab$RoomCTabFolderViewFrame(RoomsTab var1, SashForm var2, String var3, String var4, AbstractTab var5) {
      super(var2, var3, var4, var5);
      this.this$0 = var1;
      this.createViewListener(new RoomsTab$RoomCTabFolderViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("t.r.closeClosed", "x-light", new RoomsTab$4(this));
      this.addToolItem("t.r.closeAll", "x", new RoomsTab$5(this));
   }

   // $VF: synthetic method
   static RoomsTab access$000(RoomsTab$RoomCTabFolderViewFrame var0) {
      return var0.this$0;
   }
}
