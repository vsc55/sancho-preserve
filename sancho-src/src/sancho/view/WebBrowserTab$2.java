package sancho.view;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.custom.CTabItem;

class WebBrowserTab$2 implements StatusTextListener {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$2(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void changed(StatusTextEvent var1) {
      Browser var2 = (Browser)var1.widget;
      if (var2 != null && !var2.isDisposed()) {
         CTabItem var3 = (CTabItem)var2.getData("cTabItem");
         if (var3 == this.this$0.cTabFolder.getSelection()) {
            this.this$0.getMainWindow().getStatusline().setText(var1.text);
         }
      }
   }
}
