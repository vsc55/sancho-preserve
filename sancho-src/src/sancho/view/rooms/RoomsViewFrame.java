package sancho.view.rooms;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;

public class RoomsViewFrame extends TabbedSashViewFrame {
   public RoomsViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4, "rooms");
      this.gView = new RoomsTableView(this);
      this.createViewListener(new RoomsViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }
}
