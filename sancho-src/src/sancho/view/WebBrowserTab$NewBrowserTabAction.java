package sancho.view;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import sancho.view.utility.SResources;

public class WebBrowserTab$NewBrowserTabAction extends Action {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   public WebBrowserTab$NewBrowserTabAction(WebBrowserTab var1) {
      super(SResources.getString("l.newBrowserTab"));
      this.this$0 = var1;
   }

   public void run() {
      Browser var1 = this.this$0.createBrowserTab();
      if (var1 != null) {
         CTabItem var2 = (CTabItem)var1.getData("cTabItem");
         this.this$0.cTabFolder.setSelection(var2);
      }
   }
}
