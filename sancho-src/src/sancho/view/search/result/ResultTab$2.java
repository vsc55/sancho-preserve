package sancho.view.search.result;

import sancho.model.mldonkey.utility.SearchWaiting;
import sancho.view.utility.SResources;

class ResultTab$2 implements Runnable {
   // $VF: synthetic field
   private final SearchWaiting val$searchWaiting;
   // $VF: synthetic field
   private final ResultTab this$0;

   ResultTab$2(ResultTab var1, SearchWaiting var2) {
      this.this$0 = var1;
      this.val$searchWaiting = var2;
   }

   public void run() {
      if (ResultTab.access$200(this.this$0) != null && !ResultTab.access$200(this.this$0).isDisposed()) {
         ResultTab.access$200(this.this$0).setText(SResources.getString("s.r.searchesWaiting") + this.val$searchWaiting.getNumWaiting());
         ResultTab.access$200(this.this$0).getParent().layout();
      }
   }
}
