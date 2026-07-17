package sancho.view;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import sancho.view.preferences.PreferenceLoader;

class WebBrowserTab$10 extends SelectionAdapter {
   // $VF: synthetic field
   private final int val$num;
   // $VF: synthetic field
   private final WebBrowserTab$WebBrowserViewFrame this$1;

   WebBrowserTab$10(WebBrowserTab$WebBrowserViewFrame var1, int var2) {
      this.this$1 = var1;
      this.val$num = var2;
   }

   public void widgetSelected(SelectionEvent var1) {
      WebBrowserTab$WebBrowserViewFrame.access$000(this.this$1).navigate(PreferenceLoader.loadString("webBrowserToolItem" + this.val$num));
   }
}
