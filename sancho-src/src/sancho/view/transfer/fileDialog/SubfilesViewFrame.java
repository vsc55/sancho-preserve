package sancho.view.transfer.fileDialog;

import org.eclipse.swt.widgets.Composite;
import sancho.model.mldonkey.IPreview;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class SubfilesViewFrame extends ViewFrame {
   public SubfilesViewFrame(Composite var1, String var2, String var3, AbstractTab var4, IPreview var5) {
      super(var1, var2, var3, var4);
      this.gView = new SubfilesTableView(this, var5);
      this.createViewListener(new SubfilesViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
