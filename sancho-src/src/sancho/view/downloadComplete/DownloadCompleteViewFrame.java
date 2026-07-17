package sancho.view.downloadComplete;

import org.eclipse.swt.widgets.Composite;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class DownloadCompleteViewFrame extends ViewFrame {
   public DownloadCompleteViewFrame(Composite var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new DownloadCompleteTableView(this);
      this.createViewListener(new DownloadCompleteViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
