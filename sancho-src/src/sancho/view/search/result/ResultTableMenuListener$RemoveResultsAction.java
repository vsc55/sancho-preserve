package sancho.view.search.result;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class ResultTableMenuListener$RemoveResultsAction extends Action {
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$RemoveResultsAction(ResultTableMenuListener var1) {
      super(SResources.getString("b.remove"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("minus"));
   }

   public void run() {
      this.this$0.removeSelected();
   }
}
