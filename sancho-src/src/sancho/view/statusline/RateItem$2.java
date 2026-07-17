package sancho.view.statusline;

import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;

class RateItem$2 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final RateItem this$0;

   RateItem$2(RateItem var1, MyObservable var2) {
      this.this$0 = var1;
      this.val$o = var2;
   }

   public void run() {
      if (RateItem.access$000(this.this$0)) {
         this.this$0.updateClientStats((ClientStats)this.val$o);
      }
   }
}
