package sancho.view.transfer.fileComments;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.File;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class FileCommentsViewFrame extends ViewFrame {
   public FileCommentsViewFrame(Composite composite, String labelString, String imageString, AbstractTab tab, File file) {
      super(composite, labelString, imageString, tab);
      this.gView = new FileCommentsTableView(this, file);
      this.createViewListener(new FileCommentsViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
