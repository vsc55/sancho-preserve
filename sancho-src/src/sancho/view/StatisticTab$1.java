package sancho.view;

import sancho.model.mldonkey.Network;

class StatisticTab$1 implements Runnable {
   // $VF: synthetic field
   private final Network val$network;
   // $VF: synthetic field
   private final StatisticTab this$0;

   StatisticTab$1(StatisticTab var1, Network var2) {
      this.this$0 = var1;
      this.val$network = var2;
   }

   public void run() {
      this.this$0.updateNetworkTab(this.val$network);
   }
}
