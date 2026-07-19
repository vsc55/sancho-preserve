package sancho.view.statistics.networkStats;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;

public class NetworkStatsViewFrame extends SashViewFrame {
   Network network;

   public NetworkStatsViewFrame(SashForm var1, String var2, String var3, AbstractTab var4, Network var5, NetworkStatCollection var6) {
      super(var1, var2, var3, var4);
      this.network = var5;
      this.gView = new NetworkStatsTableView(this, var5, var6);
      this.createViewListener(new NetworkStatsViewListener(this));
      this.createViewToolBar();
   }

   public void refreshInThread() {
      if (!this.gView.isDisposed()) {
         this.gView.getComposite().getDisplay().asyncExec(new Runnable() {
            public void run() {
               if (!NetworkStatsViewFrame.this.gView.isDisposed()) {
                  if (NetworkStatsViewFrame.this.gView.isActive() && NetworkStatsViewFrame.this.gView.isVisible()) {
                     NetworkStatsViewFrame.this.gView.clearAll();
                     ((NetworkStatsTableView)NetworkStatsViewFrame.this.gView).updateHeader();
                  } else {
                     NetworkStatsViewFrame.this.gView.getContentProvider().setNeedsRefresh(true);
                  }
               }
            }
         });
      }
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("mi.refresh", "rotate", new SelectionAdapter() {
         public void widgetSelected(SelectionEvent var1) {
            if (Sancho.hasCollectionFactory()) {
               NetworkStatsViewFrame.this.network.getStats();
            }
         }
      });
   }
}
