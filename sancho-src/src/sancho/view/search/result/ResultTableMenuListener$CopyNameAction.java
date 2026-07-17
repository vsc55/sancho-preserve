package sancho.view.search.result;

import org.eclipse.jface.action.Action;
import sancho.model.mldonkey.Result;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

class ResultTableMenuListener$CopyNameAction extends Action {
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$CopyNameAction(ResultTableMenuListener var1) {
      super(SResources.getString("s.r.copyName"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      String var1 = "";
      String var2 = System.getProperty("line.separator");

      for (int var3 = 0; var3 < ResultTableMenuListener.access$600(this.this$0).size(); var3++) {
         Result var4 = (Result)ResultTableMenuListener.access$700(this.this$0).get(var3);
         if (var1.length() > 0) {
            var1 = var1 + var2;
         }

         var1 = var1 + var4.getName();
      }

      MainWindow.copyToClipboard(var1);
   }
}
