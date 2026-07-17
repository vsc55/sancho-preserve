package sancho.view.rooms;

import sancho.model.mldonkey.RoomCollection;
import sancho.view.utility.SResources;

class RoomsTableContentProvider$1 implements Runnable {
   // $VF: synthetic field
   private final RoomCollection val$roomCollection;
   // $VF: synthetic field
   private final RoomsTableContentProvider this$0;

   RoomsTableContentProvider$1(RoomsTableContentProvider var1, RoomCollection var2) {
      this.this$0 = var1;
      this.val$roomCollection = var2;
   }

   public void run() {
      if (RoomsTableContentProvider.access$000(this.this$0) != null && !RoomsTableContentProvider.access$100(this.this$0).isDisposed()) {
         if (this.val$roomCollection.removed()) {
            RoomsTableContentProvider.access$200(this.this$0).remove(this.val$roomCollection.getAndClearRemoved());
            this.this$0.updateHeaderLabel();
         }

         if (this.val$roomCollection.added()) {
            RoomsTableContentProvider.access$300(this.this$0).add(this.val$roomCollection.getAndClearAdded());
            this.this$0.updateHeaderLabel();
         }

         if (this.val$roomCollection.updated()) {
            RoomsTableContentProvider.access$400(this.this$0).update(this.val$roomCollection.getAndClearUpdated(), SResources.SA_Z);
         }
      }
   }
}
