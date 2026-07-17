package sancho.view.statistics.networks;

import org.eclipse.swt.custom.SashForm;
import sancho.view.utility.AbstractTab;
import sancho.view.viewFrame.SashViewFrame;
import sancho.view.viewer.GView;

public class NetworksViewFrame extends SashViewFrame {
   public NetworksViewFrame(SashForm var1, String var2, String var3, AbstractTab var4) {
      super(var1, var2, var3, var4);
      this.gView = new NetworksTableView(this);
      this.createViewListener(new NetworksViewListener(this));
      this.createViewToolBar();
   }

   public void refreshInThread() {
      if (!this.gView.isDisposed()) {
         this.gView.getComposite().getDisplay().asyncExec(new NetworksViewFrame$1(this));
      }
   }

   public void createViewToolBar() {
      super.createViewToolBar();
   }

   // $VF: synthetic method
   static GView access$000(NetworksViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$100(NetworksViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$200(NetworksViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$300(NetworksViewFrame var0) {
      return var0.gView;
   }

   // $VF: synthetic method
   static GView access$400(NetworksViewFrame var0) {
      return var0.gView;
   }
}
