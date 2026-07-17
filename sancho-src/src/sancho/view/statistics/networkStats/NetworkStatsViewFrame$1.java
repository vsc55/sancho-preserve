package sancho.view.statistics.networkStats;

class NetworkStatsViewFrame$1 implements Runnable {
   // $VF: synthetic field
   private final NetworkStatsViewFrame this$0;

   NetworkStatsViewFrame$1(NetworkStatsViewFrame var1) {
      this.this$0 = var1;
   }

   public void run() {
      if (!NetworkStatsViewFrame.access$000(this.this$0).isDisposed()) {
         if (NetworkStatsViewFrame.access$100(this.this$0).isActive() && NetworkStatsViewFrame.access$200(this.this$0).isVisible()) {
            NetworkStatsViewFrame.access$400(this.this$0).clearAll();
            ((NetworkStatsTableView)NetworkStatsViewFrame.access$500(this.this$0)).updateHeader();
         } else {
            NetworkStatsViewFrame.access$300(this.this$0).getContentProvider().setNeedsRefresh(true);
         }
      }
   }
}
