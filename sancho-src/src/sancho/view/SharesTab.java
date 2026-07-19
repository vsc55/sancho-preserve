package sancho.view;

import org.eclipse.swt.widgets.Composite;
import sancho.view.shares.UploadViewFrame;
import sancho.view.utility.AbstractTab;

public class SharesTab extends AbstractTab {
   public SharesTab(MainWindow mainWindow, String name) {
      super(mainWindow, name);
   }

   protected void createContents(Composite composite) {
      this.addViewFrame(new UploadViewFrame(composite, "l.uploads", "up_arrow_blue", this));
   }
}
