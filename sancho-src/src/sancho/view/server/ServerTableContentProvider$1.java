package sancho.view.server;

import sancho.model.mldonkey.ServerCollection;
import sancho.utility.MyObservable;
import sancho.view.utility.SResources;

class ServerTableContentProvider$1 implements Runnable {
   // $VF: synthetic field
   private final MyObservable val$o;
   // $VF: synthetic field
   private final ServerTableContentProvider this$0;

   ServerTableContentProvider$1(ServerTableContentProvider var1, MyObservable var2) {
      this.this$0 = var1;
      this.val$o = var2;
   }

   public void run() {
      if (ServerTableContentProvider.access$000(this.this$0) != null && !ServerTableContentProvider.access$100(this.this$0).isDisposed()) {
         ServerCollection var1 = (ServerCollection)this.val$o;
         boolean var2 = false;
         if (var1.removed()) {
            ServerTableContentProvider.access$200(this.this$0).remove(var1.getAndClearRemoved());
            var2 = true;
         }

         if (var1.added()) {
            ServerTableContentProvider.access$300(this.this$0).add(var1.getAndClearAdded());
            var2 = true;
         }

         if (var1.updated()) {
            boolean var3 = ServerTableContentProvider.access$400(this.this$0).updateOrRefresh(var1.getAndClearUpdated(), SResources.SA_Z);
            var2 = !var3;
         }

         if (var2) {
            this.this$0.updateLabel(var1);
         }
      }
   }
}
