package sancho.view;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class WebBrowserTab$9 extends SelectionAdapter {
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$9(WebBrowserTab var1) {
      this.this$0 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      CTabItem var2 = (CTabItem)var1.item;
      Browser var3 = (Browser)var2.getData("browser");
      if (var3 != null && !var3.isDisposed()) {
         this.this$0.inputCombo.setText(var3.getUrl());
      }

      this.this$0.inputCombo.setFocus();
      this.this$0.viewFrame.updateCLabelText(var2.getText());
   }
}
