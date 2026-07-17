package sancho.view;

import org.eclipse.jface.action.Action;
import sancho.view.utility.SResources;

public class WebBrowserTab$ADRBookmark extends Action {
   String URL;
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   public WebBrowserTab$ADRBookmark(WebBrowserTab var1, String var2, String var3) {
      this.this$0 = var1;
      this.setText(var1.formatTitle(var2));
      this.setImageDescriptor(SResources.getImageDescriptor("web-link-o"));
      this.URL = var3;
   }

   public void run() {
      if (this.URL != null && !this.URL.equals("")) {
         this.this$0.navigate(this.this$0.getSelectedBrowser(), this.URL);
      }
   }
}
