package sancho.view;

import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Combo;
import sancho.view.transfer.UniformResourceLocator;

class WebBrowserTab$1 extends DropTargetAdapter {
   // $VF: synthetic field
   private final UniformResourceLocator val$uRL;
   // $VF: synthetic field
   private final Combo val$linkEntryCombo;
   // $VF: synthetic field
   private final WebBrowserTab this$0;

   WebBrowserTab$1(WebBrowserTab var1, UniformResourceLocator var2, Combo var3) {
      this.this$0 = var1;
      this.val$uRL = var2;
      this.val$linkEntryCombo = var3;
   }

   public void dragEnter(DropTargetEvent var1) {
      var1.detail = 1;

      for (int var2 = 0; var2 < var1.dataTypes.length; var2++) {
         if (this.val$uRL.isSupportedType(var1.dataTypes[var2])) {
            var1.detail = 4;
            break;
         }
      }
   }

   public void drop(DropTargetEvent var1) {
      if (var1.data != null) {
         this.val$linkEntryCombo.setText((String)var1.data);
      }
   }
}
