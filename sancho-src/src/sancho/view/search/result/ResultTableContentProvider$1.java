package sancho.view.search.result;

import sancho.model.mldonkey.utility.SearchWaiting;
import sancho.utility.MyObservable;
import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;

class ResultTableContentProvider$1 implements Runnable {
   // $VF: synthetic field
   private final Object val$arg1;
   // $VF: synthetic field
   private final MyObservable val$myObservable;
   // $VF: synthetic field
   private final int val$flag;
   // $VF: synthetic field
   private final ResultTableContentProvider this$0;

   ResultTableContentProvider$1(ResultTableContentProvider var1, Object var2, MyObservable var3, int var4) {
      this.this$0 = var1;
      this.val$arg1 = var2;
      this.val$myObservable = var3;
      this.val$flag = var4;
   }

   public void run() {
      if (this.val$arg1 == null) {
         ObjectMap var1 = (ObjectMap)this.val$myObservable;
         if (ResultTableContentProvider.access$000(this.this$0) == null || ResultTableContentProvider.access$100(this.this$0).isDisposed()) {
            return;
         }

         switch (this.val$flag) {
            case 0:
               if (var1.added()) {
                  ResultTableContentProvider.access$300(this.this$0).add(var1.getAndClearAdded());
                  this.this$0.updateHeaderLabel();
               }
               break;
            case 1:
               if (var1.updated()) {
                  ResultTableContentProvider.access$400(this.this$0).update(var1.getAndClearUpdated(), SResources.SA_Z);
                  this.this$0.updateHeaderLabel();
               }
               break;
            case 2:
               if (var1.removed()) {
                  ResultTableContentProvider.access$200(this.this$0).remove(var1.getAndClearRemoved());
                  this.this$0.updateHeaderLabel();
               }
         }
      } else if (this.val$arg1 instanceof SearchWaiting) {
         SearchWaiting var2 = (SearchWaiting)this.val$arg1;
         ResultTableContentProvider.access$502(this.this$0, " (" + SResources.getString("s.r.waiting") + var2.getNumWaiting() + ")");
         this.this$0.updateHeaderLabel();
      }
   }
}
