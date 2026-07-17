package sancho.view.statistics.networkStats;

import org.eclipse.swt.custom.SashForm;
import sancho.model.mldonkey.Network;
import sancho.model.mldonkey.utility.NetworkStatCollection;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewer.GView;

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
         this.gView.getComposite().getDisplay().asyncExec(new NetworkStatsViewFrame$1(this));
      }
   }

   public void createViewToolBar() {
      super.createViewToolBar();
      this.addToolItem("mi.refresh", "rotate", new NetworkStatsViewFrame$2(this));
   }

   // $VF: synthetic method
   static GView access$000(NetworkStatsViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(NetworkStatsViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$200(NetworkStatsViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$300(NetworkStatsViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$400(NetworkStatsViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$500(NetworkStatsViewFrame var0) {
      return var0.gView;
   }
}
