package sancho.view.downloadComplete;

import org.eclipse.swt.widgets.Composite;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.ViewFrame;

public class DownloadCompleteViewFrame extends ViewFrame {
   public DownloadCompleteViewFrame(Composite composite, String titleKey, String imageKey, AbstractTab tab) {
      super(composite, titleKey, imageKey, tab);
      this.gView = new DownloadCompleteTableView(this);
      this.createViewListener(new DownloadCompleteViewListener(this));
      this.createViewToolBar();
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addRefine();
   }
}
