package sancho.view.friends;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.ToolItem;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.TabbedSashViewFrame;

public class FriendsViewFrame extends TabbedSashViewFrame {
   public FriendsViewFrame(SashForm sashForm, String name, String text, AbstractTab tab) {
      super(sashForm, name, text, tab, "friends");
      this.gView = new FriendsTableView(this);
      this.createViewListener(new FriendsViewListener(this));
      this.createViewToolBar();
      this.switchToTab(this.cTabFolder.getItems()[0]);
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      new ToolItem(this.toolBar, 2);
      this.addRefine();
   }
}
