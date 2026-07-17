package sancho.view.statusline;

import org.eclipse.jface.action.Action;
import sancho.view.MainWindow;
import sancho.view.utility.SResources;

public class RateItem$CopyLabelsAction extends Action {
   // $VF: synthetic field
   private final RateItem this$0;

   public RateItem$CopyLabelsAction(RateItem var1) {
      super(SResources.getString("mi.copy"));
      this.this$0 = var1;
      this.setImageDescriptor(SResources.getImageDescriptor("copy"));
   }

   public void run() {
      String var1 = "";
      if (RateItem.access$200(this.this$0) != null && !RateItem.access$200(this.this$0).isDisposed()) {
         var1 = var1 + RateItem.access$200(this.this$0).getText();
      }

      if (RateItem.access$300(this.this$0) != null && !RateItem.access$300(this.this$0).isDisposed()) {
         String var2 = RateItem.access$300(this.this$0).getText();
         if (!var2.equals("")) {
            var1 = var1 + " | " + var2;
         }
      }

      MainWindow.copyToClipboard(var1);
   }
}
