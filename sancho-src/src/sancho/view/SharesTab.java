package sancho.view;

import org.eclipse.swt.widgets.Composite;
import sancho.view.shares.UploadViewFrame;
import sancho.view.utility.AbstractTab;

public class SharesTab extends AbstractTab {
   public SharesTab(MainWindow var1, String var2) {
      super(var1, var2);
   }

   protected void createContents(Composite var1) {
      this.addViewFrame(new UploadViewFrame(var1, "l.uploads", "up_arrow_blue", this));
   }
}
