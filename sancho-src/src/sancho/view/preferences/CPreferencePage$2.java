package sancho.view.preferences;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.ScrollBar;

class CPreferencePage$2 extends ControlAdapter {
   // $VF: synthetic field
   private final CPreferencePage this$0;

   CPreferencePage$2(CPreferencePage var1) {
      this.this$0 = var1;
   }

   public void controlResized(ControlEvent var1) {
      ScrolledComposite var2 = (ScrolledComposite)var1.widget;
      int var3 = var2.getClientArea().height;
      if (var3 > 10) {
         var3 -= 10;
      }

      ScrollBar var4 = var2.getVerticalBar();
      if (var4 != null) {
         var4.setIncrement(15);
         var4.setPageIncrement(var3);
      }
   }
}
