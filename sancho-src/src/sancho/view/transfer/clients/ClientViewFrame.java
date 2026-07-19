package sancho.view.transfer.clients;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;
import sancho.view.viewer.GView;

public class ClientViewFrame extends TabbedSashViewFrame {
   public ClientViewFrame(SashForm sashForm, String title, String imageString, AbstractTab tab, GView gView) {
      super(sashForm, title, imageString, tab, "clients");
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
