package sancho.view.statistics.networks;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class NetworksViewFrame extends SashViewFrame {
   public NetworksViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
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
