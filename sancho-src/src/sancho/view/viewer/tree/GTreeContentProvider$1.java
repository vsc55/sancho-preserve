package sancho.view.viewer.tree;

import sancho.utility.MyObservable;

class GTreeContentProvider$1 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final Object val$object;
   // $VF: synthetic field
   private final int val$flag;
   // $VF: synthetic field
   private final GTreeContentProvider this$0;

   GTreeContentProvider$1(GTreeContentProvider var1, MyObservable var2, Object var3, int var4) {
      this.this$0 = var1;
      this.val$o = var2;
      this.val$object = var3;
      this.val$flag = var4;
   }

   public void run() {
      this.this$0.onUpdate(this.val$o, this.val$object, this.val$flag);
   }
}
