package sancho.view.statistics.networkStats;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.core.Sancho;

class NetworkStatsViewFrame$2 extends SelectionAdapter {
   // $VF: synthetic field
   private final NetworkStatsViewFrame this$0;

   NetworkStatsViewFrame$2(NetworkStatsViewFrame var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      if (Sancho.hasCollectionFactory()) {
         this.this$0.network.getStats();
      }
   }
}
