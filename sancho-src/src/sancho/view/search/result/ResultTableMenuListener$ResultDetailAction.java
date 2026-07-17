package sancho.view.search.result;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Result;
import sancho.view.utility.SResources;

class ResultTableMenuListener$ResultDetailAction extends Action {
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$ResultDetailAction(ResultTableMenuListener var1) {
      super(SResources.getString("s.r.resultDetails"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("info"));
   }

   public void run() {
      Result var1 = (Result)ResultTableMenuListener.access$400(this.this$0).get(0);
      if (var1 instanceof Result) {
         new ResultDetailDialog(ResultTableMenuListener.access$500(this.this$0).getShell(), var1).open();
      }
   }
}
