package sancho.view.statistics.networks;

class NetworksViewFrame$1 implements Runnable {
   // $VF: synthetic field
   private final NetworksViewFrame this$0;

   NetworksViewFrame$1(NetworksViewFrame var1) {
      this.this$0 = var1;
   }

   public void run() {
      if (!NetworksViewFrame.access$000(this.this$0).isDisposed()) {
         if (NetworksViewFrame.access$100(this.this$0).isActive() && NetworksViewFrame.access$200(this.this$0).isVisible()) {
            NetworksViewFrame.access$400(this.this$0).refresh();
         } else {
            NetworksViewFrame.access$300(this.this$0).getContentProvider().setNeedsRefresh(true);
         }
      }
   }
}
