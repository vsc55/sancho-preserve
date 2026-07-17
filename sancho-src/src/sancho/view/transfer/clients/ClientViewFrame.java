package sancho.view.transfer.clients;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewer.GView;

public class ClientViewFrame extends TabbedSashViewFrame {
   public ClientViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, GView var5) {
      super(var1, var2, var3, var4, "clients");
      this.gView = new ClientTableView(this);
      this.createViewListener(new ClientViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }
}
