package sancho.view;

import sancho.utility.MyObservable;

class RoomsTab$3 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final Object val$obj;
   // $VF: synthetic field
   private final RoomsTab this$0;

   RoomsTab$3(RoomsTab var1, MyObservable var2, Object var3) {
      this.this$0 = var1;
      this.val$o = var2;
      this.val$obj = var3;
   }

   public void run() {
      this.this$0.runUpdate(this.val$o, this.val$obj);
   }
}
