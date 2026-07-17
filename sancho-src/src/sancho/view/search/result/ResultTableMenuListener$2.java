package sancho.view.search.result;

import sancho.model.mldonkey.Result;

class ResultTableMenuListener$2 implements Runnable {
   // $VF: synthetic field
   private final Result val$result;
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   ResultTableMenuListener$2(ResultTableMenuListener var1, Result var2) {
      this.this$0 = var1;
      this.val$result = var2;
   }

   public void run() {
      if (ResultTableMenuListener.access$000(this.this$0) != null
         && ResultTableMenuListener.access$100(this.this$0).getTable() != null
         && !ResultTableMenuListener.access$200(this.this$0).getTable().isDisposed()) {
         ResultTableMenuListener.access$300(this.this$0).update(new Object[]{this.val$result}, null);
      }
   }
}
