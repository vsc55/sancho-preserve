package sancho.view;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.custom.CTabItem;

class WebBrowserTab$3 implements TitleListener {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$3(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void changed(TitleEvent var1) {
      Browser var2 = (Browser)var1.widget;
      if (var2 != null && !var2.isDisposed()) {
         CTabItem var3 = (CTabItem)var2.getData("cTabItem");
         var3.setText(var1.title);
         if (var3 != null && var3 == this.this$0.cTabFolder.getSelection()) {
            this.this$0.viewFrame.updateCLabelText(var1.title);
         }
      }
   }
}
