package sancho.view.shares;

import sancho.model.mldonkey.SharedFileCollection;
import sancho.view.utility.SResources;

class UploadTableContentProvider$1 implements Runnable {
   // $VF: synthetic field
   private final SharedFileCollection val$sharedFileCollection;
   // $VF: synthetic field
   private final UploadTableContentProvider this$0;

   UploadTableContentProvider$1(UploadTableContentProvider var1, SharedFileCollection var2) {
      this.this$0 = var1;
      this.val$sharedFileCollection = var2;
   }

   public void run() {
      if (UploadTableContentProvider.access$000(this.this$0) != null && !UploadTableContentProvider.access$100(this.this$0).isDisposed()) {
         if (this.val$sharedFileCollection.removed()) {
            UploadTableContentProvider.access$200(this.this$0).remove(this.val$sharedFileCollection.getAndClearRemoved());
         }

         if (this.val$sharedFileCollection.added()) {
            UploadTableContentProvider.access$300(this.this$0).add(this.val$sharedFileCollection.getAndClearAdded());
         }

         if (this.val$sharedFileCollection.updated()) {
            UploadTableContentProvider.access$400(this.this$0).update(this.val$sharedFileCollection.getAndClearUpdated(), SResources.SA_Z);
         }
      }
   }
}
