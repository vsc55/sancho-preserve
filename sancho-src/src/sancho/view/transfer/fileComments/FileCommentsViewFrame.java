package sancho.view.transfer.fileComments;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.File;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class FileCommentsViewFrame extends ViewFrame {
   public FileCommentsViewFrame(Composite var1, String var2, String var3, AbstractTab var4, File var5) {
      super(var1, var2, var3, var4);
      this.gView = new FileCommentsTableView(this, var5);
      this.createViewListener(new FileCommentsViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
