package sancho.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

class WebBrowserTab$15 extends SelectionAdapter {
   // $VF: synthetic field
   private final WebBrowserTab$WebBrowserViewFrame this$1;

   WebBrowserTab$15(WebBrowserTab$WebBrowserViewFrame var1) {
      this.this$1 = var1;
   }

   public void widgetSelected(SelectionEvent var1) {
      WebBrowserTab$WebBrowserViewFrame.access$000(this.this$1).browserForward();
   }
}
