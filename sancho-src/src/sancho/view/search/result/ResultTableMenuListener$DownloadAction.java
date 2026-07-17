package sancho.view.search.result;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

class ResultTableMenuListener$DownloadAction extends Action {
   // $VF: synthetic field
   private final ResultTableMenuListener this$0;

   public ResultTableMenuListener$DownloadAction(ResultTableMenuListener var1) {
      super(SResources.getString("s.r.download"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("down_arrow_green"));
   }

   public void run() {
      this.this$0.downloadSelected();
   }
}
