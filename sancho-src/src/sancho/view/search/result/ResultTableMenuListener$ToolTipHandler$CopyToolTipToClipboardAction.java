package sancho.view.search.result;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class ResultTableMenuListener$ToolTipHandler$CopyToolTipToClipboardAction extends Action {
   // $VF: synthetic field
   private final ResultTableMenuListener$ToolTipHandler this$1;

   public ResultTableMenuListener$ToolTipHandler$CopyToolTipToClipboardAction(ResultTableMenuListener$ToolTipHandler var1) {
      this.this$1 = var1;
      this.setText(SResources.getString("copy to clipboard"));
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      if (ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1) != null) {
         MainWindow.copyToClipboard(ResultTableMenuListener$ToolTipHandler.access$1200(this.this$1).getToolTip());
      }
   }
}
