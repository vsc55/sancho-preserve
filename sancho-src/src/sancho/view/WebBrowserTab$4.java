package sancho.view;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.CTabItem;

class WebBrowserTab$4 implements CloseWindowListener {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$4(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void close(WindowEvent var1) {
      Browser var2 = (Browser)var1.widget;
      if (var2 != null && !var2.isDisposed()) {
         CTabItem var3 = (CTabItem)var2.getData("cTabItem");
         if (var3 != null && !var3.isDisposed() && !this.this$0.cTabFolder.isDisposed()) {
            var3.dispose();
         }
      }
   }
}
