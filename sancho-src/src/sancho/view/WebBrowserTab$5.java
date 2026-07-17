package sancho.view;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.WindowEvent;

class WebBrowserTab$5 implements OpenWindowListener {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$5(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void open(WindowEvent var1) {
      Browser var2 = this.this$0.createBrowserTab();
      if (var2 != null) {
         var1.browser = var2;
      }
   }
}
