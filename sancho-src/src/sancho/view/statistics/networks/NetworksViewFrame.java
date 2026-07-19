package sancho.view.statistics.networks;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class NetworksViewFrame extends SashViewFrame {
   public NetworksViewFrame(SashForm sashForm, String preferenceString, String tooltip, AbstractTab tab) {
      super(sashForm, preferenceString, tooltip, tab);
      this.gView = new NetworksTableView(this);
      this.createViewListener(new NetworksViewListener(this));
      this.createViewToolBar();
   }

   public void refreshInThread() {
      if (!this.gView.isDisposed()) {
         this.gView.getComposite().getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (!NetworksViewFrame.this.gView.isDisposed()) {
                  if (NetworksViewFrame.this.gView.isActive() && NetworksViewFrame.this.gView.isVisible()) {
                     NetworksViewFrame.this.gView.refresh();
                  } else {
                     NetworksViewFrame.this.gView.getContentProvider().setNeedsRefresh(true);
                  }
               }
            }
         });
      }
   }

   public void createViewToolBar() {
      super.createViewToolBar();
   }
}
