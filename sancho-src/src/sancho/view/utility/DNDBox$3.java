package sancho.view.utility;

import sancho.model.mldonkey.ClientStats;
import sancho.utility.MyObservable;

class DNDBox$3 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final DNDBox this$0;

   DNDBox$3(DNDBox var1, MyObservable var2) {
      this.this$0 = var1;
      this.val$o = var2;
   }

   public void run() {
      this.this$0.redrawImage((ClientStats)this.val$o);
   }
}
