package sancho.view.transfer.fileDialog;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class SubfilesViewFrame extends ViewFrame {
   public SubfilesViewFrame(Composite composite, String prefString, String labelText, AbstractTab tab, IPreview preview) {
      super(composite, prefString, labelText, tab);
      this.gView = new SubfilesTableView(this, preview);
      this.createViewListener(new SubfilesViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
