package sancho.view;

import sancho.model.mldonkey.ClientStats;
import sancho.utility.SwissArmy;
import sancho.view.statistics.GraphData;

class StatisticTab$2 implements Runnable {
   // $VF: synthetic field
   private final ClientStats val$clientStats;
   // $VF: synthetic field
   private final StatisticTab this$0;

   StatisticTab$2(StatisticTab var1, ClientStats var2) {
      this.this$0 = var1;
      this.val$clientStats = var2;
   }

   public void run() {
      if (StatisticTab.access$000(this.this$0) != null && !StatisticTab.access$000(this.this$0).isDisposed()) {
         StatisticTab.access$000(this.this$0)
            .setText(this.writeLabel(StatisticTab.access$100(this.this$0).getGraphData(), StatisticTab.access$200(), this.val$clientStats.getUploadCounter()));
         StatisticTab.access$500(this.this$0)
            .setText(this.writeLabel(StatisticTab.access$300(this.this$0).getGraphData(), StatisticTab.access$400(), this.val$clientStats.getDownloadCounter()));
      }
   }

   public String writeLabel(GraphData var1, String var2, long var3) {
      StringBuffer var5 = new StringBuffer(32);
      var5.append(var2);
      var5.append(": ");
      var5.append(SwissArmy.calcStringSize(var3));
      var5.append(" ");
      var5.append(StatisticTab.access$600());
      var5.append(", ");
      var5.append((double)var1.getAvg() / 100.0);
      var5.append(" ");
      var5.append(StatisticTab.access$700());
      var5.append(", ");
      var5.append((double)var1.getMax() / 100.0);
      var5.append(" ");
      var5.append(StatisticTab.access$800());
      if (StatisticTab.access$900(this.this$0) > 0) {
         var5.append(" ");
         var5.append(" (");
         var5.append(StatisticTab.access$900(this.this$0));
         var5.append("s ");
         var5.append(StatisticTab.access$1000());
         var5.append(")");
      }

      return var5.toString();
   }
}
