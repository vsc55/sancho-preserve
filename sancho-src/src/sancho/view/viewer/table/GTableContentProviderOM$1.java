package sancho.view.viewer.table;

import sancho.utility.ObjectMap;
import sancho.view.utility.SResources;

class GTableContentProviderOM$1 implements Runnable {
   // $VF: synthetic field
   private final int val$flag;
   // $VF: synthetic field
   private final ObjectMap val$objectMap;
   // $VF: synthetic field
   private final GTableContentProviderOM this$0;

   GTableContentProviderOM$1(GTableContentProviderOM var1, int var2, ObjectMap var3) {
      this.this$0 = var1;
      this.val$flag = var2;
      this.val$objectMap = var3;
   }

   public void run() {
      if (this.this$0.gView != null && !this.this$0.gView.isDisposed()) {
         switch (this.val$flag) {
            case 0:
               if (this.val$objectMap.added()) {
                  this.this$0.tableViewer.add(this.val$objectMap.getAndClearAdded());
                  this.this$0.updateHeaderLabel(this.val$objectMap.size());
               }
               break;
            case 1:
               if (this.val$objectMap.updated()) {
                  this.this$0.tableViewer.update(this.val$objectMap.getAndClearUpdated(), SResources.SA_Z);
                  if (this.this$0.updateOnUpdate) {
                     this.this$0.updateHeaderLabel(this.val$objectMap.size());
                  }
               }
               break;
            case 2:
               if (this.val$objectMap.removed()) {
                  this.this$0.tableViewer.remove(this.val$objectMap.getAndClearRemoved());
                  this.this$0.updateHeaderLabel(this.val$objectMap.size());
               }
         }
      }
   }
}
